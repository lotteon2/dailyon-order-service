package com.dailyon.orderservice.domain.order.facade;

import com.dailyon.orderservice.domain.delivery.service.DeliveryService;
import com.dailyon.orderservice.domain.delivery.service.request.DeliveryServiceRequest;
import com.dailyon.orderservice.domain.gift.entity.Gift;
import com.dailyon.orderservice.domain.gift.service.GiftService;
import com.dailyon.orderservice.domain.order.api.request.TOrderDto;
import com.dailyon.orderservice.domain.order.clients.*;
import com.dailyon.orderservice.domain.order.clients.dto.AuctionProductDTO;
import com.dailyon.orderservice.domain.order.dynamo.document.TOrder;
import com.dailyon.orderservice.domain.order.entity.Order;
import com.dailyon.orderservice.domain.order.entity.OrderDetail;
import com.dailyon.orderservice.domain.order.entity.enums.OrderType;
import com.dailyon.orderservice.domain.order.facade.response.OrderDetailResponse;
import com.dailyon.orderservice.domain.order.facade.response.OrderPageResponse;
import com.dailyon.orderservice.domain.order.message.kafka.event.OrderEventProducer;
import com.dailyon.orderservice.domain.order.message.kafka.event.dto.RefundDTO;
import com.dailyon.orderservice.domain.order.message.sqs.OrderSqsProducer;
import com.dailyon.orderservice.domain.order.message.sqs.dto.RawNotificationData;
import com.dailyon.orderservice.domain.order.message.sqs.dto.SQSNotificationDto;
import com.dailyon.orderservice.domain.order.service.OrderService;
import com.dailyon.orderservice.domain.order.service.request.TOrderCommand;
import com.dailyon.orderservice.domain.refund.entity.Refund;
import com.dailyon.orderservice.domain.refund.service.RefundService;
import dailyon.domain.order.clients.CouponDTO;
import dailyon.domain.order.clients.PaymentDTO;
import dailyon.domain.order.clients.ProductDTO;
import dailyon.domain.order.clients.ProductRankResponse;
import dailyon.domain.order.kafka.OrderDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.dailyon.orderservice.common.utils.OrderValidator.validateMemberPoint;
import static com.dailyon.orderservice.domain.order.entity.enums.OrderType.AUCTION;
import static com.dailyon.orderservice.domain.order.entity.enums.OrderType.GIFT;
import static com.dailyon.orderservice.domain.order.message.sqs.OrderSqsProducer.*;
import static com.dailyon.orderservice.domain.order.message.sqs.dto.RawNotificationData.forGiftReceived;
import static com.dailyon.orderservice.domain.order.message.sqs.dto.RawNotificationData.forOrderComplete;
import static com.dailyon.orderservice.domain.order.message.sqs.dto.SQSNotificationDto.of;
import static java.util.Collections.EMPTY_LIST;

@Service
@RequiredArgsConstructor
public class OrderFacade {
  private final OrderService orderService;
  private final DeliveryService deliveryService;
  private final RefundService refundService;
  private final GiftService giftService;

  private final ProductFeignClient productFeignClient;
  private final PromotionFeignClient promotionFeignClient;
  private final MemberFeignClient memberFeignClient;
  private final PaymentFeignClient paymentFeignClient;
  private final AuctionClient auctionClient;
  private final OrderEventProducer producer;
  private final OrderSqsProducer orderSqsProducer;
  private final TOrderDtoMapper tOrderDtoMapper;

  public String orderReady(TOrderDto.TOrderCreateRequest request, Long memberId) {
    TOrder tOrder = createTOrder(request, memberId);
    if (GIFT == request.getType()) {
      createGift(request, tOrder);
    }
    return orderPaymentReady(memberId, toPaymentReadyParam(tOrder, request.getUsedPoints()));
  }

  private TOrder createTOrder(TOrderDto.TOrderCreateRequest request, Long memberId) {
    return orderService.createTOrder(extractCommand(request, memberId), memberId);
  }

  private void createGift(TOrderDto.TOrderCreateRequest request, TOrder tOrder) {
    giftService.createGift(tOrderDtoMapper.toGiftCommand(request), tOrder.getId());
  }

  private PaymentDTO.PaymentReadyParam toPaymentReadyParam(TOrder tOrder, int usedPoints) {
    return tOrderDtoMapper.toPaymentReadyParam(tOrder, "KAKAOPAY", usedPoints);
  }

  private String orderPaymentReady(Long memberId, PaymentDTO.PaymentReadyParam param) {
    return paymentFeignClient.orderPaymentReady(memberId, param);
  }

  public String orderApprove(TOrderDto.OrderApproveRequest request, String orderNo) {
    TOrder tOrder = orderService.getTOrder(orderNo);
    OrderDTO orderDTO = tOrderDtoMapper.of(tOrder, request.getPg_token());
    producer.orderCreated(orderDTO);
    return tOrder.getId();
  }

  private List<CouponDTO.ProductCouponDTO> getProductCoupons(
      List<CouponDTO.CouponParam> couponParams, Long memberId) {
    return couponParams.isEmpty()
        ? EMPTY_LIST
        : promotionFeignClient.getProductCoupons(memberId, couponParams);
  }

  private List<ProductDTO.OrderProductListDTO.OrderProductDTO> getOrderProducts(
      List<ProductDTO.OrderProductParam> orderProductParams) {
    return productFeignClient.getOrderProducts(orderProductParams).getResponse();
  }

  private int getMemberPoints(Long memberId) {
    return memberFeignClient.getMyPoints(memberId);
  }

  private TOrderCommand.RegisterTOrder extractCommand(
      TOrderDto.TOrderCreateRequest request, Long memberId) {
    validateMemberPoint(getMemberPoints(memberId), request.getUsedPoints());
    return isAuction(request)
        ? createAuctionOrder(request, memberId)
        : createNormalOrder(request, memberId);
  }

  private boolean isAuction(TOrderDto.TOrderCreateRequest request) {
    return request.getType().equals(AUCTION);
  }

  private TOrderCommand.RegisterTOrder createAuctionOrder(
      TOrderDto.TOrderCreateRequest request, Long memberId) {
    AuctionProductDTO auctionProductInfo = getAuctionProductInfo(memberId, request.getAuctionId());
    var orderProducts = auctionProductInfo.createOrderProducts();
    return tOrderDtoMapper.of(
        request, EMPTY_LIST, orderProducts, auctionProductInfo.getOrderPrice());
  }

  private AuctionProductDTO getAuctionProductInfo(Long memberId, String auctionId) {
    AuctionProductDTO auctionProductInfo = auctionClient.getAuctionProductInfo(memberId, auctionId);
    if (!auctionProductInfo.isWinner()) {
      throw new RuntimeException("경매 낙찰자가 아닙니다.");
    }
    return auctionProductInfo;
  }

  private TOrderCommand.RegisterTOrder createNormalOrder(
      TOrderDto.TOrderCreateRequest request, Long memberId) {
    var orderItemList = request.getOrderItems();
    var productCoupons = getProductCoupons(tOrderDtoMapper.toCouponParams(orderItemList), memberId);
    var orderProducts = getOrderProducts(tOrderDtoMapper.toOrderProductParams(orderItemList));
    return tOrderDtoMapper.of(request, productCoupons, orderProducts, null);
  }

  public String orderCreate(String orderNo) {
    TOrder tOrder = orderService.getTOrder(orderNo);
    Order order = orderService.createOrder(tOrder);

    if (GIFT.equals(OrderType.valueOf(tOrder.getType()))) {
      Gift gift = giftService.update(order);

      RawNotificationData notificationData = forGiftReceived(gift.getReceiverName());
      SQSNotificationDto notificationDto = of(gift.getReceiverId(), notificationData);
      orderSqsProducer.produce(GIFT_RECEIVED_NOTIFICATION_QUEUE, notificationDto);
    } else {
      deliveryService.createDelivery(DeliveryServiceRequest.from(tOrder.getDelivery()));

      RawNotificationData notificationData =
          forOrderComplete(order.getOrderNo(), order.getTotalAmount());
      SQSNotificationDto notificationDto = of(order.getMemberId(), notificationData);
      orderSqsProducer.produce(ORDER_COMPLETE_NOTIFICATION_QUEUE, notificationDto);
    }
    return tOrder.getId();
  }

  public OrderPageResponse getOrders(Pageable pageable, OrderType type, Long memberId) {
    Page<Order> page = orderService.getOrders(pageable, type, memberId);
    return OrderPageResponse.from(page);
  }

  public List<OrderDetailResponse> getOrderDetails(String orderNo, Long memberId) {
    List<OrderDetail> orderDetails = orderService.getOrderDetails(orderNo, memberId);
    return OrderDetailResponse.from(orderDetails);
  }

  @Transactional
  public Long cancelOrderDetail(String OrderDetailNo, Long memberId) {
    OrderDetail orderDetail = orderService.cancelOrderDetail(OrderDetailNo, memberId);
    Refund refund = refundService.createRefund(orderDetail);
    producer.createRefund(orderDetail.getOrderDetailNo(), RefundDTO.of(orderDetail, refund));

    RawNotificationData notificationData =
        RawNotificationData.forOrderCanceled(
            orderDetail.getOrderPrice(),
            orderDetail.getProductName(),
            orderDetail.getProductQuantity());
    SQSNotificationDto notificationDto = of(orderDetail.getOrder().getMemberId(), notificationData);
    orderSqsProducer.produce(ORDER_CANCELED_NOTIFICATION_QUEUE, notificationDto);
    return refund.getId();
  }

  public List<ProductRankResponse> getMostSoldProducts() {
    return orderService.getMostSoldProducts();
  }
}

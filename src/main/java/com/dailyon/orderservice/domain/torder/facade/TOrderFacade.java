package com.dailyon.orderservice.domain.torder.facade;

import com.dailyon.orderservice.domain.gift.service.GiftService;
import com.dailyon.orderservice.domain.torder.api.request.TOrderDto;
import com.dailyon.orderservice.domain.torder.api.request.TOrderDto.TOrderCreateRequest;
import com.dailyon.orderservice.domain.torder.clients.*;
import com.dailyon.orderservice.domain.torder.clients.dto.AuctionProductDTO;
import com.dailyon.orderservice.domain.torder.entity.TOrder;
import com.dailyon.orderservice.domain.torder.kafka.event.TOrderEventProducer;
import com.dailyon.orderservice.domain.torder.service.TOrderService;
import com.dailyon.orderservice.domain.torder.service.request.TOrderCommand.RegisterTOrder;
import dailyon.domain.order.clients.CouponDTO.CouponParam;
import dailyon.domain.order.clients.CouponDTO.ProductCouponDTO;
import dailyon.domain.order.clients.PaymentDTO.PaymentReadyParam;
import dailyon.domain.order.clients.ProductDTO.OrderProductListDTO.OrderProductDTO;
import dailyon.domain.order.clients.ProductDTO.OrderProductParam;
import dailyon.domain.order.kafka.OrderDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.dailyon.orderservice.common.utils.OrderValidator.validateMemberPoint;
import static com.dailyon.orderservice.domain.order.entity.enums.OrderType.AUCTION;
import static com.dailyon.orderservice.domain.order.entity.enums.OrderType.GIFT;
import static java.util.Collections.EMPTY_LIST;

@Service
@RequiredArgsConstructor
public class TOrderFacade {

  private final TOrderService tOrderService;
  private final GiftService giftService;
  private final ProductFeignClient productFeignClient;
  private final PromotionFeignClient promotionFeignClient;
  private final MemberFeignClient memberFeignClient;
  private final PaymentFeignClient paymentFeignClient;
  private final AuctionClient auctionClient;
  private final TOrderEventProducer producer;
  private final TOrderDtoMapper tOrderDtoMapper;

  public String orderReady(TOrderCreateRequest request, Long memberId) {
    TOrder tOrder = createTOrder(request, memberId);
    if (GIFT == request.getType()) {
      createGift(request, tOrder);
    }
    return orderPaymentReady(memberId, toPaymentReadyParam(tOrder, request.getUsedPoints()));
  }

  private TOrder createTOrder(TOrderCreateRequest request, Long memberId) {
    return tOrderService.createTOrder(extractCommand(request, memberId), memberId);
  }

  private void createGift(TOrderCreateRequest request, TOrder tOrder) {
    giftService.createGift(tOrderDtoMapper.toGiftCommand(request), tOrder.getId());
  }

  private PaymentReadyParam toPaymentReadyParam(TOrder tOrder, int usedPoints) {
    return tOrderDtoMapper.toPaymentReadyParam(tOrder, "KAKAOPAY", usedPoints);
  }

  private String orderPaymentReady(Long memberId, PaymentReadyParam param) {
    return paymentFeignClient.orderPaymentReady(memberId, param);
  }

  public String orderApprove(TOrderDto.OrderApproveRequest request, String orderNo) {
    TOrder tOrder = tOrderService.getTOrder(orderNo);
    OrderDTO orderDTO = tOrderDtoMapper.of(tOrder, request.getPg_token());
    producer.orderCreated(orderDTO);
    return tOrder.getId();
  }

  private List<ProductCouponDTO> getProductCoupons(List<CouponParam> couponParams, Long memberId) {
    return couponParams.isEmpty()
        ? EMPTY_LIST
        : promotionFeignClient.getProductCoupons(memberId, couponParams);
  }

  private List<OrderProductDTO> getOrderProducts(List<OrderProductParam> orderProductParams) {
    return productFeignClient.getOrderProducts(orderProductParams).getResponse();
  }

  private int getMemberPoints(Long memberId) {
    return memberFeignClient.getMyPoints(memberId);
  }

  private RegisterTOrder extractCommand(TOrderCreateRequest request, Long memberId) {
    validateMemberPoint(getMemberPoints(memberId), request.getUsedPoints());
    return isAuction(request)
        ? createAuctionOrder(request, memberId)
        : createNormalOrder(request, memberId);
  }

  private boolean isAuction(TOrderCreateRequest request) {
    return request.getType().equals(AUCTION);
  }

  private RegisterTOrder createAuctionOrder(TOrderCreateRequest request, Long memberId) {
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

  private RegisterTOrder createNormalOrder(TOrderCreateRequest request, Long memberId) {
    var orderItemList = request.getOrderItems();
    var productCoupons = getProductCoupons(tOrderDtoMapper.toCouponParams(orderItemList), memberId);
    var orderProducts = getOrderProducts(tOrderDtoMapper.toOrderProductParams(orderItemList));
    return tOrderDtoMapper.of(request, productCoupons, orderProducts, null);
  }
}

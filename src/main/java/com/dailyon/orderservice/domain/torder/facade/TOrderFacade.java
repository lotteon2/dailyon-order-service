package com.dailyon.orderservice.domain.torder.facade;

import com.dailyon.orderservice.domain.order.service.GiftService;
import com.dailyon.orderservice.domain.order.service.request.GiftCommand;
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
    RegisterTOrder tOrderCommand = extractCommand(request, memberId);
    TOrder tOrder = tOrderService.createTOrder(tOrderCommand, memberId);
    PaymentReadyParam param =
        tOrderDtoMapper.toPaymentReadyParam(tOrder, "KAKAOPAY", request.getUsedPoints());
    if (GIFT == request.getType()) {
      GiftCommand.RegisterGift registerGift = tOrderDtoMapper.toGiftCommand(request);
      giftService.createGift(registerGift, tOrder.getId());
    }
    String nextUrl = paymentFeignClient.orderPaymentReady(memberId, param);
    return nextUrl;
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
    var orderItemList = request.getOrderItems();
    int memberPoints = getMemberPoints(memberId);
    int usedPoints = request.getUsedPoints();
    validateMemberPoint(usedPoints, memberPoints);

    if (request.getType().equals(AUCTION)) {
      AuctionProductDTO auctionProductInfo =
          auctionClient.getAuctionProductInfo(memberId, request.getAuctionId());
      if (!auctionProductInfo.isWinner()) {
        throw new RuntimeException("경매 낙찰자가 아닙니다.");
      }
      var orderProducts = auctionProductInfo.createOrderProducts();
      return tOrderDtoMapper.of(
          request, EMPTY_LIST, orderProducts, auctionProductInfo.getOrderPrice());
    }
    var productCoupons = getProductCoupons(tOrderDtoMapper.toCouponParams(orderItemList), memberId);
    var orderProducts = getOrderProducts(tOrderDtoMapper.toOrderProductParams(orderItemList));

    RegisterTOrder tOrderCommand = tOrderDtoMapper.of(request, productCoupons, orderProducts, null);
    return tOrderCommand;
  }
}

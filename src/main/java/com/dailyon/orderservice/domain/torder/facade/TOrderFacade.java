package com.dailyon.orderservice.domain.torder.facade;

import com.dailyon.orderservice.domain.torder.api.request.TOrderDto;
import com.dailyon.orderservice.domain.torder.api.request.TOrderDto.TOrderCreateRequest;
import com.dailyon.orderservice.domain.torder.api.request.TOrderDto.TOrderCreateRequest.RegisterItemRequest;
import com.dailyon.orderservice.domain.torder.clients.MemberFeignClient;
import com.dailyon.orderservice.domain.torder.clients.PaymentFeignClient;
import com.dailyon.orderservice.domain.torder.clients.ProductFeignClient;
import com.dailyon.orderservice.domain.torder.clients.PromotionFeignClient;
import com.dailyon.orderservice.domain.torder.clients.dto.CouponDTO.CouponParam;
import com.dailyon.orderservice.domain.torder.clients.dto.CouponDTO.ProductCouponDTO;
import com.dailyon.orderservice.domain.torder.clients.dto.PaymentDTO.PaymentReadyParam;
import com.dailyon.orderservice.domain.torder.clients.dto.ProductDTO.OrderProductListDTO.OrderProductDTO;
import com.dailyon.orderservice.domain.torder.clients.dto.ProductDTO.OrderProductParam;
import com.dailyon.orderservice.domain.torder.entity.TOrder;
import com.dailyon.orderservice.domain.torder.kafka.event.TOrderEventProducer;
import com.dailyon.orderservice.domain.torder.kafka.event.dto.OrderDTO;
import com.dailyon.orderservice.domain.torder.service.TOrderService;
import com.dailyon.orderservice.domain.torder.service.request.TOrderCommand.RegisterTOrder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static com.dailyon.orderservice.common.utils.OrderValidator.validateMemberPoint;
import static java.util.Collections.EMPTY_LIST;
import static java.util.stream.Collectors.toMap;

@Service
@RequiredArgsConstructor
public class TOrderFacade {

  private final TOrderService tOrderService;
  private final ProductFeignClient productFeignClient;
  private final PromotionFeignClient promotionFeignClient;
  private final MemberFeignClient memberFeignClient;
  private final PaymentFeignClient paymentFeignClient;
  private final TOrderEventProducer producer;
  private final TOrderDtoMapper tOrderDtoMapper;

  public String orderReady(TOrderCreateRequest request, Long memberId) {
    var orderItemList = request.getOrderItems();

    var productCoupons = getProductCoupons(tOrderDtoMapper.toCouponParams(orderItemList), memberId);
    var orderProducts = getOrderProducts(tOrderDtoMapper.toOrderProductParams(orderItemList));

    int memberPoints = getMemberPoints(memberId);
    int usedPoints = request.getUsedPoints();

    validateMemberPoint(usedPoints, memberPoints);

    RegisterTOrder tOrderCommand = tOrderDtoMapper.of(request, productCoupons, orderProducts);
    TOrder tOrder = tOrderService.createTOrder(tOrderCommand, memberId);

    PaymentReadyParam paymentReadyParam =
        PaymentReadyParam.of(tOrder, "KAKAOPAY", usedPoints); // TODO : method, usedPoints 나중에 바꿈;
    String nextUrl = paymentFeignClient.orderPaymentReady(memberId, paymentReadyParam);
    return nextUrl;
  }

  public String orderApprove(TOrderDto.OrderApproveRequest request, String orderNo) {
    TOrder tOrder = tOrderService.getTOrder(orderNo);
    producer.orderCreated(OrderDTO.of(tOrder, request.getPg_token()));
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

  private Map<Long, ProductCouponDTO> extractProductCouponToMap(
      List<ProductCouponDTO> productCoupons) {
    return productCoupons.stream()
        .collect(toMap(ProductCouponDTO::getProductId, Function.identity()));
  }

  private int getMemberPoints(Long memberId) {
    return memberFeignClient.getMyPoints(memberId);
  }
}

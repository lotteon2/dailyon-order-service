package com.dailyon.orderservice.domain.torder.facade;

import com.dailyon.orderservice.domain.torder.clients.ProductFeignClient;
import com.dailyon.orderservice.domain.torder.clients.PromotionFeignClient;
import com.dailyon.orderservice.domain.torder.clients.dto.CouponDTO.CouponParam;
import com.dailyon.orderservice.domain.torder.clients.dto.CouponDTO.ProductCouponDTO;
import com.dailyon.orderservice.domain.torder.clients.dto.ProductDTO.OrderProductListDTO.OrderProductDTO;
import com.dailyon.orderservice.domain.torder.clients.dto.ProductDTO.OrderProductParam;
import com.dailyon.orderservice.domain.torder.facade.request.TOrderFacadeRequest.TOrderFacadeCreateRequest;
import com.dailyon.orderservice.domain.torder.facade.request.TOrderFacadeRequest.TOrderFacadeCreateRequest.OrderProductInfo;
import com.dailyon.orderservice.domain.torder.service.TOrderService;
import com.dailyon.orderservice.domain.torder.service.request.TOrderServiceRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static java.util.Collections.EMPTY_LIST;
import static java.util.stream.Collectors.toMap;

@Service
@RequiredArgsConstructor
public class TOrderFacade {

  private final TOrderService tOrderService;
  private final ProductFeignClient productFeignClient;
  private final PromotionFeignClient promotionFeignClient;


  /**
   * ready 결제 준비 성공 시 QR Code를 반환한다.
   * @param request
   * @param memberId
   * @return String
   */
  public String orderReady(TOrderFacadeCreateRequest request, Long memberId) {

    List<ProductCouponDTO> productCoupons = getProductCoupons(request.toCouponParams(), memberId);
    List<OrderProductDTO> orderProducts =
        getOrderProducts(request.toOrderProductParams(), memberId);

    Map<Long, OrderProductInfo> orderProductMap = request.extractOrderInfoToMap();
    Map<Long, ProductCouponDTO> productCouponMap = extractProductCouponToMap(productCoupons);

    return tOrderService.createTOrder(
        TOrderServiceRequest.of(orderProducts, orderProductMap, productCouponMap, request.getType()), memberId);
  }

  private List<ProductCouponDTO> getProductCoupons(List<CouponParam> couponParams, Long memberId) {
    return couponParams.isEmpty()
        ? EMPTY_LIST
        : promotionFeignClient.getProductCoupons(memberId, couponParams);
  }

  private List<OrderProductDTO> getOrderProducts(
      List<OrderProductParam> orderProductParams, Long memberId) {
    return productFeignClient.getOrderProducts(memberId, orderProductParams).getResponse();
  }

  private Map<Long, ProductCouponDTO> extractProductCouponToMap(
      List<ProductCouponDTO> productCoupons) {
    return productCoupons.stream()
        .collect(toMap(ProductCouponDTO::getProductId, Function.identity()));
  }
}

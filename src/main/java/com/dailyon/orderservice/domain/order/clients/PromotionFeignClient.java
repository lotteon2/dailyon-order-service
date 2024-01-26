package com.dailyon.orderservice.domain.order.clients;

import com.dailyon.orderservice.config.DefaultFeignConfig;
import dailyon.domain.order.clients.CouponDTO.CouponParam;
import dailyon.domain.order.clients.CouponDTO.ProductCouponDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.List;

@FeignClient(
    name = "promotion-service",
    url = "${endpoint.promotion-service}",
    configuration = DefaultFeignConfig.class)
public interface PromotionFeignClient {

  @PostMapping("/clients/coupons/validate-for-order")
  List<ProductCouponDTO> getProductCoupons(
      @RequestHeader(value = "memberId") Long memberId,
      @RequestBody List<CouponParam> couponParams);
}

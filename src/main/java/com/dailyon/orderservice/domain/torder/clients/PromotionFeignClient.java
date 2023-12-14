package com.dailyon.orderservice.domain.torder.clients;

import com.dailyon.orderservice.config.DefaultFeignConfig;
import com.dailyon.orderservice.domain.torder.clients.dto.CouponDTO;
import com.dailyon.orderservice.domain.torder.clients.dto.CouponDTO.CouponParam;
import com.dailyon.orderservice.domain.torder.clients.dto.CouponDTO.ProductCouponDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.List;

@FeignClient(name = "promotion-service", url = "${endpoint.promotion-service}",configuration = DefaultFeignConfig.class)
public interface PromotionFeignClient {

  @PostMapping("/clients/coupons/validate-for-order")
  List<ProductCouponDTO> getProductCoupons(
      @RequestHeader(value = "memberId") Long memberId,
      @RequestBody List<CouponParam> couponParams);
}

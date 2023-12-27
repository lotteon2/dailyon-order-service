package com.dailyon.orderservice.domain.torder.clients.dto;

import lombok.*;

public class CouponDTO {

  @Getter
  @Builder
  @ToString
  public static class CouponParam {
    private Long productId;
    private Long categoryId;
    private Long couponInfoId;
  }

  @Getter
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  public static class ProductCouponDTO {
    private Long productId;
    private String discountType;
    private Long discountValue;
    private String couponName;
    private Long couponInfoId;
    private Long minPurchaseAmount;
    private Long maxDiscountAmount;
  }
}

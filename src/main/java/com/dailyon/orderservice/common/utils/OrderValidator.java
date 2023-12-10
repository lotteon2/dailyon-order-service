package com.dailyon.orderservice.common.utils;

import com.dailyon.orderservice.common.exception.InvalidParamException;
import com.dailyon.orderservice.domain.torder.clients.dto.CouponDTO.ProductCouponDTO;

import java.util.Optional;

public abstract class OrderValidator {

  public static void validateCouponInfo(
      int discountPrice, int totalPerchaseAmount, Optional<ProductCouponDTO> coupon) {
    if (coupon.isEmpty()) return;
    ProductCouponDTO getCoupon = coupon.get();
    if (discountPrice > getCoupon.getMaxDiscountAmount()
        || totalPerchaseAmount < getCoupon.getMinPurchaseAmount()) {
      throw new InvalidParamException();
    }
  }
}

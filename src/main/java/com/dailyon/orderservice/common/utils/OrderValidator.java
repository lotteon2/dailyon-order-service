package com.dailyon.orderservice.common.utils;

import com.dailyon.orderservice.common.exception.InvalidParamException;
import com.dailyon.orderservice.domain.torder.clients.dto.CouponDTO.ProductCouponDTO;
import com.dailyon.orderservice.domain.torder.exception.InsufficientStockException;

import java.util.Optional;

public abstract class OrderValidator {

  public static void validateCouponInfo(
      int discountPrice, int totalPurchaseAmount, Optional<ProductCouponDTO> coupon) {
    if (coupon.isEmpty()) return;
    ProductCouponDTO getCoupon = coupon.get();
    if (totalPurchaseAmount < getCoupon.getMinPurchaseAmount()) {
      throw new InvalidParamException();
    }
  }

  public static void validateStock(int quantity, int stock) {
    if (quantity > stock) {
      throw new InsufficientStockException();
    }
  }
}

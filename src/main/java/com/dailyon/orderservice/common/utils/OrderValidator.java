package com.dailyon.orderservice.common.utils;

import com.dailyon.orderservice.common.exception.InvalidParamException;
import com.dailyon.orderservice.domain.order.entity.enums.OrderDetailStatus;
import com.dailyon.orderservice.domain.torder.clients.dto.CouponDTO.ProductCouponDTO;
import com.dailyon.orderservice.domain.torder.exception.InsufficientPointException;
import com.dailyon.orderservice.domain.torder.exception.InsufficientStockException;

import java.util.Optional;

public abstract class OrderValidator {

  public static void validateCouponInfo(int totalPurchaseAmount, Optional<ProductCouponDTO> coupon) {
    if (coupon.isEmpty()) return;
    ProductCouponDTO getCoupon = coupon.get();
    if (totalPurchaseAmount < getCoupon.getMinPurchaseAmount()) {
      throw new InvalidParamException();
    }
  }

  public static void validateStock(int quantity, int stock) {
    if (stock == 0) return;
    if (quantity > stock) {
      throw new InsufficientStockException();
    }
  }

  public static void validateMemberPoint(int usedPoints, int memberPoints) {
    if (usedPoints == 0) return;
    if (usedPoints > memberPoints) {
      throw new InsufficientPointException();
    }
  }
}

package com.dailyon.orderservice.common.utils;

import com.dailyon.orderservice.domain.order.service.TOrderCommand;
import dailyon.domain.order.clients.CouponDTO.ProductCouponDTO;
import dailyon.domain.order.clients.ProductDTO.OrderProductListDTO.OrderProductDTO;

public class OrderCalculator {

  private static final String FIXED_AMOUNT = "FIXED_AMOUNT";
  private static final String PERCENTAGE = "PERCENTAGE";

  public static int calculateDiscountPrice(
      OrderProductDTO orderProduct, ProductCouponDTO coupon, Integer quantity) {
    if (coupon == null) {
      return 0;
    }
    switch (coupon.getDiscountType()) {
      case FIXED_AMOUNT:
        return coupon.getDiscountValue().intValue();
      case PERCENTAGE:
        return coupon.getMaxDiscountAmount() != 0
            ? Math.min(
                (int)
                    ((orderProduct.getPrice() * quantity)
                        * (coupon.getDiscountValue().intValue() * 0.01)),
                (int) coupon.getMaxDiscountAmount())
            : (int)
                ((orderProduct.getPrice() * quantity)
                    * (coupon.getDiscountValue().intValue() * 0.01));
      default:
        throw new IllegalArgumentException(
            "잘못된 형식의 쿠폰 타입 : " + coupon.getDiscountType()); // TODO : 예외 처리
    }
  }

  public static int calculateOrderPrice(
      OrderProductDTO orderProduct,
      TOrderCommand.RegisterOrderItem orderItem,
      int discountedPrice) {
    return (orderProduct.getPrice() * orderItem.getQuantity()) - discountedPrice;
  }
}

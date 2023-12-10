package com.dailyon.orderservice.common.utils;

import com.dailyon.orderservice.domain.torder.clients.dto.CouponDTO.ProductCouponDTO;
import com.dailyon.orderservice.domain.torder.clients.dto.ProductDTO.OrderProductListDTO.OrderProductDTO;
import com.dailyon.orderservice.domain.torder.facade.request.TOrderFacadeRequest.TOrderFacadeCreateRequest.OrderProductInfo;

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
        return Math.min(
            (int)
                ((orderProduct.getPrice() * quantity)
                    * (coupon.getDiscountValue().intValue() * 0.01)),
            coupon.getMaxDiscountAmount().intValue());
      default:
        throw new IllegalArgumentException(
            "잘못된 형식의 쿠폰 타입 : " + coupon.getDiscountType()); // TODO : 예외 처리
    }
  }

  public static int calculateOrderPrice(
      OrderProductDTO orderProduct, OrderProductInfo orderProductInfo, int discountedPrice) {
    return (orderProduct.getPrice() * orderProductInfo.getQuantity()) - discountedPrice;
  }
}

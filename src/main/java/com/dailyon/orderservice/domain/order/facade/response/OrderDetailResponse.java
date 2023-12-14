package com.dailyon.orderservice.domain.order.facade.response;

import com.dailyon.orderservice.domain.order.entity.OrderDetail;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class OrderDetailResponse {

  private Long id;
  private String orderNo;
  private String productName;
  private Integer productQuantity;
  private String thumbnail;
  private Integer orderPrice;
  private String couponName;
  private Integer couponDiscountPrice;
  private String status;

  @Builder
  private OrderDetailResponse(
      Long id,
      String orderNo,
      String productName,
      Integer productQuantity,
      String thumbnail,
      Integer orderPrice,
      String couponName,
      Integer couponDiscountPrice,
      String status) {
    this.id = id;
    this.orderNo = orderNo;
    this.productName = productName;
    this.productQuantity = productQuantity;
    this.thumbnail = thumbnail;
    this.orderPrice = orderPrice;
    this.couponName = couponName;
    this.couponDiscountPrice = couponDiscountPrice;
    this.status = status;
  }

  public static OrderDetailResponse from(OrderDetail orderDetail) {
    return OrderDetailResponse.builder()
        .id(orderDetail.getId())
        .orderNo(orderDetail.getOrderNo())
        .productName(orderDetail.getProductName())
        .productQuantity(orderDetail.getProductQuantity())
        .thumbnail(orderDetail.getProductImgUrl())
        .orderPrice(orderDetail.getOrderPrice())
        .couponName(orderDetail.getCouponName())
        .couponDiscountPrice(orderDetail.getCouponDiscountPrice())
        .status(orderDetail.getStatus().getMessage())
        .build();
  }
}

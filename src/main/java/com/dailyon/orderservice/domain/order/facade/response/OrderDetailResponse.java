package com.dailyon.orderservice.domain.order.facade.response;

import com.dailyon.orderservice.domain.order.entity.OrderDetail;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
public class OrderDetailResponse {

  private String orderDetailNo;
  private String orderNo;
  private String productName;
  private Integer productQuantity;
  private String thumbnail;
  private Integer orderPrice;
  private String couponName;
  private Integer couponDiscountPrice;
  private String status;
  private boolean reviewCheck;

  @Builder
  private OrderDetailResponse(
      String orderDetailNo,
      String orderNo,
      String productName,
      Integer productQuantity,
      String thumbnail,
      Integer orderPrice,
      String couponName,
      Integer couponDiscountPrice,
      String status,
      boolean reviewCheck) {
    this.orderDetailNo = orderDetailNo;
    this.orderNo = orderNo;
    this.productName = productName;
    this.productQuantity = productQuantity;
    this.thumbnail = thumbnail;
    this.orderPrice = orderPrice;
    this.couponName = couponName;
    this.couponDiscountPrice = couponDiscountPrice;
    this.status = status;
    this.reviewCheck = reviewCheck;
  }

  public static OrderDetailResponse from(OrderDetail orderDetail) {
    return OrderDetailResponse.builder()
        .orderDetailNo(orderDetail.getOrderDetailNo())
        .orderNo(orderDetail.getOrderNo())
        .productName(orderDetail.getProductName())
        .productQuantity(orderDetail.getProductQuantity())
        .thumbnail(orderDetail.getProductImgUrl())
        .orderPrice(orderDetail.getOrderPrice())
        .couponName(orderDetail.getCouponName())
        .couponDiscountPrice(orderDetail.getCouponDiscountPrice())
        .status(orderDetail.getStatus().getMessage())
        .reviewCheck(orderDetail.getReviewCheck())
        .build();
  }

  public static List<OrderDetailResponse> from(List<OrderDetail> orderDetails) {
    return orderDetails.stream()
        .map(OrderDetailResponse::from)
        .collect(Collectors.toUnmodifiableList());
  }
}

package com.dailyon.orderservice.domain.order.facade.response;

import com.dailyon.orderservice.domain.order.entity.Order;
import com.dailyon.orderservice.domain.torder.entity.TOrder;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class OrderResponse {
  private String orderNo;
  private Long memberId;
  private String productsName;
  private int usedPoints;
  private int totalCouponDiscountPrice;
  private Long totalAmount;
  private String status;
  private String type;

  @JsonFormat(
      shape = JsonFormat.Shape.STRING,
      pattern = "yyyy-MM-dd HH:mm:ss",
      timezone = "Asia/Seoul")
  private LocalDateTime createdAt;

  @Builder
  private OrderResponse(
      String orderNo,
      Long memberId,
      String productsName,
      int usedPoints,
      int totalCouponDiscountPrice,
      Long totalAmount,
      String status,
      String type,
      LocalDateTime createdAt) {
    this.orderNo = orderNo;
    this.memberId = memberId;
    this.productsName = productsName;
    this.usedPoints = usedPoints;
    this.totalCouponDiscountPrice = totalCouponDiscountPrice;
    this.totalAmount = totalAmount;
    this.status = status;
    this.type = type;
    this.createdAt = createdAt;
  }

  public static OrderResponse from(Order order) {
    return OrderResponse.builder()
        .orderNo(order.getOrderNo())
        .memberId(order.getMemberId())
        .productsName(order.getProductsName())
        .usedPoints(order.getUsedPoints())
        .totalCouponDiscountPrice(order.getTotalCouponDiscountPrice())
        .totalAmount(order.getTotalAmount())
        .status(order.getStatus().getMessage())
        .type(order.getType().getMessage())
        .createdAt(order.getCreatedAt())
        .build();
  }

  public static OrderResponse from(TOrder tOrder) {
    return OrderResponse.builder()
        .orderNo(tOrder.getId())
        .memberId(tOrder.getMemberId())
        .productsName(tOrder.getProductsName())
        .usedPoints(tOrder.getUsedPoints())
        .totalCouponDiscountPrice(tOrder.getTotalCouponDiscountPrice())
        .totalAmount(tOrder.getTotalAmount())
        .status(tOrder.getStatus())
        .type(tOrder.getType())
        .createdAt(tOrder.getCreatedAt())
        .build();
  }
}

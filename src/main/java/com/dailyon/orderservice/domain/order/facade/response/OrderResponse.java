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
  private String productsName;
  private int usedPoints;
  private Long totalAmount;
  private String status;

  @JsonFormat(
      shape = JsonFormat.Shape.STRING,
      pattern = "yyyy-MM-dd HH:mm:ss",
      timezone = "Asia/Seoul")
  private LocalDateTime createdAt;

  @Builder
  private OrderResponse(
      String orderNo,
      String productsName,
      int usedPoints,
      Long totalAmount,
      String status,
      LocalDateTime createdAt) {
    this.orderNo = orderNo;
    this.productsName = productsName;
    this.usedPoints = usedPoints;
    this.totalAmount = totalAmount;
    this.status = status;
    this.createdAt = createdAt;
  }

  public static OrderResponse from(Order order) {
    return OrderResponse.builder()
        .orderNo(order.getOrderNo())
        .productsName(order.getProductsName())
        .usedPoints(order.getUsedPoints())
        .totalAmount(order.getTotalAmount())
        .status(order.getStatus().getMessage())
        .createdAt(order.getCreatedAt())
        .build();
  }

  public static OrderResponse from(TOrder tOrder) {
    return OrderResponse.builder()
        .orderNo(tOrder.getId())
        .productsName(tOrder.getProductsName())
        .totalAmount(tOrder.getTotalAmount())
        .status(tOrder.getStatus())
        .createdAt(tOrder.getCreatedAt())
        .build();
  }
}

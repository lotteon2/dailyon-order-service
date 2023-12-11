package com.dailyon.orderservice.domain.order.entity.enums;

import lombok.Getter;

@Getter
public enum OrderDetailStatus {
  PENDING("주문 접수"),
  COMPLETED("주문 완료"),
  REFUND("환불 완료");

  private final String message;

  OrderDetailStatus(String message) {
    this.message = message;
  }
}

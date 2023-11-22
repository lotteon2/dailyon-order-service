package com.dailyon.orderservice.domain.order.entity.enums;

import lombok.Getter;

@Getter
public enum OrderStatus {
  CANCELED("주문 취소"),
  COMPLETED("주문 완료");

  private final String message;

  OrderStatus(String message) {
    this.message = message;
  }
}

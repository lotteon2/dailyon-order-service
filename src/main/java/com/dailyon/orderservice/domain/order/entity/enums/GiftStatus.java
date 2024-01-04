package com.dailyon.orderservice.domain.order.entity.enums;

import lombok.Getter;

@Getter
public enum GiftStatus {
  PENDING("주문 접수"),
  ORDER_COMPLETE("주문 완료"),
  ACCEPT("선물 수락");

  private final String message;

  GiftStatus(String message) {
    this.message = message;
  }
}

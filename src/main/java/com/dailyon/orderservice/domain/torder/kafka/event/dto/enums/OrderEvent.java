package com.dailyon.orderservice.domain.torder.kafka.event.dto.enums;

import lombok.Getter;

@Getter
public enum OrderEvent {
  PENDING("주문 대기"),
  STOCK_FAIL("재고 부족"),
  POINT_FAIL("포인트 부족"),
  COUPON_FAIL("쿠폰 만료"),
  PAYMENT_FAIL("결제 실패");

  private final String message;

  OrderEvent(String message) {
    this.message = message;
  }
}

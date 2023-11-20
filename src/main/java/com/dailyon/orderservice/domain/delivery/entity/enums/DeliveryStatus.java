package com.dailyon.orderservice.domain.delivery.entity.enums;

import lombok.Getter;

@Getter
public enum DeliveryStatus {
  READY("배송준비"),
  SHIPPING("배송중"),
  COMPLETED("배송 완료"),
  CANCEL("배송취소");

  private final String message;

  DeliveryStatus(String message) {
    this.message = message;
  }
}

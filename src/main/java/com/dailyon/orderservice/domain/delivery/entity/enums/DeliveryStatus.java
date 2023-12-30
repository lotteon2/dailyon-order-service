package com.dailyon.orderservice.domain.delivery.entity.enums;

import lombok.Getter;

@Getter
public enum DeliveryStatus {
  BEFORE_DELIVERY("배송전"),
  DELIVERY_PREPARE("배송준비중"),
  DELIVERING("배송중"),
  COMPLETE_DELIVERY("배송완료");

  private final String message;

  DeliveryStatus(String message) {
    this.message = message;
  }
}

package com.dailyon.orderservice.domain.order.entity.enums;

import lombok.Getter;

@Getter
public enum OrderDetailStatus {
  BEFORE_DELIVERY("배송전"),
  DELIVERY_PREPARE("배송준비중"),
  DELIVERING("배송중"),
  COMPLETE_DELIVERY("배송완료"),
  CANCEL("취소완료");

  private final String message;

  OrderDetailStatus(String message) {
    this.message = message;
  }
}

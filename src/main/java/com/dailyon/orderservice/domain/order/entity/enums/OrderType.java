package com.dailyon.orderservice.domain.order.entity.enums;

import lombok.Getter;

@Getter
public enum OrderType {
  SINGLE("단건"),
  CART("장바구니"),
  GIFT("선물"),
  AUCTION("경매");

  private final String message;

  OrderType(String message) {
    this.message = message;
  }
}

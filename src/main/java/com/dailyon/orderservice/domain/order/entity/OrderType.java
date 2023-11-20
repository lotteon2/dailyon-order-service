package com.dailyon.orderservice.domain.order.entity;

import lombok.Getter;

@Getter
public enum OrderType {
  SINGLE("단건주문"),
  CART("장바구니주문"),
  GIFT("선물하기"),
  AUCTION("경매주문");

  private final String message;

  OrderType(String message) {
    this.message = message;
  }
}

package com.dailyon.orderservice.domain.torder.clients.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

public class PaymentDTO {

  @Getter
  @Builder
  @ToString
  public static class PaymentReadyParam {
    private String orderId;
    private String method; // 결제수단 ex) KAKAOPAY
    private String productName;
    private Integer quantity;
    private Integer totalAmount;
    private int deliveryFee;
    private int totalCouponDiscountPrice;
    private int usedPoints;
  }
}

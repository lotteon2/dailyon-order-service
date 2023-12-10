package com.dailyon.orderservice.domain.torder.clients.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class PaymentDTO {

  @Getter
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
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

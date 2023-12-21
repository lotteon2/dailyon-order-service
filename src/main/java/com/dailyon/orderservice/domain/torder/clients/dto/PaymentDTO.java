package com.dailyon.orderservice.domain.torder.clients.dto;

import com.dailyon.orderservice.domain.torder.entity.TOrder;
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

    public static PaymentReadyParam of(
        TOrder order, String method, Integer usedPoints) { // TODO : 결제 수단 enum 관리
      return PaymentReadyParam.builder()
          .orderId(order.getId())
          .method(method)
          .productName(
              order.getOrderDetails().size() == 1
                  ? order.getOrderDetails().get(0).getProductName()
                  : order.getOrderDetails().get(0).getProductName()
                      + "외 "
                      + (order.getOrderDetails().size() - 1)
                      + "항목")
          .deliveryFee(0)
          .quantity(order.getOrderDetails().size())
          .totalAmount(order.getTotalAmount().intValue())
          .totalCouponDiscountPrice(order.calculateTotalCouponDiscountPrice())
          .usedPoints(usedPoints)
          .build();
    }
  }
}

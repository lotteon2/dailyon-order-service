package com.dailyon.orderservice.domain.order.facade.response;

import com.dailyon.orderservice.domain.order.entity.Gift;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class GiftResponse {
  private String orderNo;
  private String senderName;
  private String receiverName;
  private String status;
  private String productName;

  public static GiftResponse from(Gift gift) {
    return GiftResponse.builder()
        .orderNo(gift.getOrderNo())
        .senderName(gift.getSenderName())
        .receiverName(gift.getReceiverName())
        .status(gift.getStatus().getMessage())
        .productName(gift.getOrder().getProductsName())
        .build();
  }
}

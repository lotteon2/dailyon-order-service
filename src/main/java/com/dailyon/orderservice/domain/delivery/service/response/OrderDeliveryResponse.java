package com.dailyon.orderservice.domain.delivery.service.response;

import com.dailyon.orderservice.domain.delivery.entity.Delivery;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class OrderDeliveryResponse {
  private Long id;
  private String receiver;
  private String postCode;
  private String roadAddress;
  private String detailAddress;
  private String phoneNumber;
  private String status;

  @JsonFormat(
      shape = JsonFormat.Shape.STRING,
      pattern = "yyyy-MM-dd HH:mm:ss",
      timezone = "Asia/Seoul")
  private LocalDateTime createdAt;

  private Long memberId;
  private String orderNo;
  private String productsName;

  public static OrderDeliveryResponse from(Delivery delivery) {
    return OrderDeliveryResponse.builder()
        .id(delivery.getId())
        .receiver(delivery.getReceiver())
        .postCode(delivery.getPostCode())
        .roadAddress(delivery.getRoadAddress())
        .detailAddress(delivery.getDetailAddress())
        .phoneNumber(delivery.getPhoneNumber())
        .status(delivery.getStatus().getMessage())
        .memberId(delivery.getOrder().getMemberId())
        .orderNo(delivery.getOrder().getOrderNo())
        .productsName(delivery.getOrder().getProductsName())
        .build();
  }
}

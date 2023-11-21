package com.dailyon.orderservice.domain.delivery.service.response;

import com.dailyon.orderservice.domain.delivery.entity.Delivery;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class DeliveryResponse {
  private String receiver;
  private String postCode;
  private String roadAddress;
  private String detailAddress;
  private String phoneNumber;

  @Builder
  private DeliveryResponse(
      String receiver,
      String postCode,
      String roadAddress,
      String detailAddress,
      String phoneNumber) {
    this.receiver = receiver;
    this.postCode = postCode;
    this.roadAddress = roadAddress;
    this.detailAddress = detailAddress;
    this.phoneNumber = phoneNumber;
  }

  public static DeliveryResponse from(Delivery delivery) {
    return DeliveryResponse.builder()
        .receiver(delivery.getReceiver())
        .postCode(delivery.getPostCode())
        .roadAddress(delivery.getRoadAddress())
        .detailAddress(builder().detailAddress)
        .phoneNumber(delivery.getPhoneNumber())
        .build();
  }
}

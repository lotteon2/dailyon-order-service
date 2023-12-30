package com.dailyon.orderservice.domain.delivery.api.request;

import com.dailyon.orderservice.domain.delivery.service.request.DeliveryServiceRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class DeliveryCreateRequest {
  @NotEmpty(message = "주문번호는 필수 입니다.")
  private String orderId;

  @NotEmpty(message = "수령인은 필수 입니다.")
  private String receiver;

  @NotEmpty(message = "우편번호는 필수 입니다.")
  private String postCode;

  @NotEmpty(message = "도로명 주소는 필수 입니다.")
  private String roadAddress;

  @NotEmpty(message = "상세 주소는 필수 입니다.")
  private String detailAddress;

  private String phoneNumber;

  public DeliveryServiceRequest toServiceRequest() {
    return DeliveryServiceRequest.builder()
        .orderId(orderId)
        .receiver(receiver)
        .postCode(postCode)
        .roadAddress(roadAddress)
        .detailAddress(detailAddress)
        .detailAddress(detailAddress)
        .build();
  }
}

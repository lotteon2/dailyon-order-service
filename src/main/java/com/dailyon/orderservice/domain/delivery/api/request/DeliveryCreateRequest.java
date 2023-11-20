package com.dailyon.orderservice.domain.delivery.api.request;

import com.dailyon.orderservice.domain.delivery.entity.Delivery;
import com.dailyon.orderservice.domain.order.entity.Order;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@NoArgsConstructor
@Getter
public class DeliveryCreateRequest {
  @NotNull(message = "주문번호는 필수입니다.")
  private Long orderId;

  @NotEmpty(message = "수령인은 필수 입니다.")
  private String receiver;

  @NotEmpty(message = "우편번호는 필수 입니다.")
  private String postCode;

  @NotEmpty(message = "도로명 주소는 필수 입니다.")
  private String roadAddress;

  @NotEmpty(message = "상세 주소는 필수 입니다.")
  private String detailAddress;

  private String phoneNumber;

  public Delivery toEntity(Order order) {
    return Delivery.builder()
        .order(order)
        .receiver(receiver)
        .postCode(postCode)
        .roadAddress(roadAddress)
        .detailAddress(detailAddress)
        .phoneNumber(phoneNumber)
        .build();
  }
}

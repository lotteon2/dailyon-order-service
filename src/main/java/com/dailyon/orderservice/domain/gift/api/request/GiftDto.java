package com.dailyon.orderservice.domain.gift.api.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotEmpty;

public class GiftDto {

  @Getter
  @Setter
  @ToString
  public static class createDelivery {
    @NotEmpty(message = "주문번호는 필수 입니다.")
    private String orderNo;

    @NotEmpty(message = "수령인은 필수 입니다.")
    private String receiver;

    @NotEmpty(message = "우편번호는 필수 입니다.")
    private String postCode;

    @NotEmpty(message = "도로명 주소는 필수 입니다.")
    private String roadAddress;

    @NotEmpty(message = "상세 주소는 필수 입니다.")
    private String detailAddress;

    private String phoneNumber;
  }
}

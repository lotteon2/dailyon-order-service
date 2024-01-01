package com.dailyon.orderservice.domain.order.service.request;

import com.dailyon.orderservice.domain.order.entity.Gift;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class GiftCommand {

  @Getter
  @Builder
  @ToString
  public static class RegisterGift {

    @NotNull(message = "받는사람 아이디는 필수 입니다.")
    private Long receiverId;

    @NotBlank(message = "받는사람 닉네임은 필수 입니다.")
    private String receiverName;

    @NotBlank(message = "보내는 사람 닉네임은 필수 입니다.")
    private String senderName;

    public Gift toEntity(String orderNo) {
      return Gift.builder()
          .orderNo(orderNo)
          .receiverId(receiverId)
          .receiverName(receiverName)
          .senderName(senderName)
          .build();
    }
  }
}

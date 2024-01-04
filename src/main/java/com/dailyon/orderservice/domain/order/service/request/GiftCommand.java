package com.dailyon.orderservice.domain.order.service.request;

import com.dailyon.orderservice.domain.delivery.entity.Delivery;
import com.dailyon.orderservice.domain.order.entity.Gift;
import com.dailyon.orderservice.domain.order.entity.Order;
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
    private final Long receiverId;

    @NotBlank(message = "받는사람 닉네임은 필수 입니다.")
    private final String receiverName;

    @NotBlank(message = "보내는 사람 닉네임은 필수 입니다.")
    private final String senderName;

    public Gift toEntity(String orderNo) {
      return Gift.builder()
          .orderNo(orderNo)
          .receiverId(receiverId)
          .receiverName(receiverName)
          .senderName(senderName)
          .build();
    }
  }

  @Getter
  @Builder
  @ToString
  public static class Accept {
    private final String orderNo;
    private final String receiver;
    private final String postCode;
    private final String roadAddress;
    private final String detailAddress;
    private final String phoneNumber;

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
}

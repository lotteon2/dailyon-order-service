package com.dailyon.orderservice.domain.delivery.service.request;

import com.dailyon.orderservice.domain.delivery.entity.Delivery;
import com.dailyon.orderservice.domain.order.entity.Order;
import com.dailyon.orderservice.domain.torder.entity.TDelivery;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DeliveryServiceRequest {
  private String orderId;
  private String receiver;
  private String postCode;
  private String roadAddress;
  private String detailAddress;
  private String phoneNumber;

  public static DeliveryServiceRequest from(TDelivery delivery) {
    return DeliveryServiceRequest.builder()
        .orderId(delivery.getOrderNo())
        .roadAddress(delivery.getRoadAddress())
        .detailAddress(delivery.getDetailAddress())
        .postCode(delivery.getPostCode())
        .receiver(delivery.getReceiver())
        .build();
  }

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

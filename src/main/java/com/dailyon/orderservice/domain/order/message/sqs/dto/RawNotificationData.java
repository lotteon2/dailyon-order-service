package com.dailyon.orderservice.domain.order.message.sqs.dto;

import com.dailyon.orderservice.domain.order.message.sqs.dto.enums.NotificationType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RawNotificationData {
  private String message;
  private Map<String, String> parameters;
  private NotificationType notificationType; // 알림 유형

  public static RawNotificationData forGiftReceived(String nickname) {
    Map<String, String> parameters = new HashMap<>();
    parameters.put("nickname", nickname);

    return new RawNotificationData(null, parameters, NotificationType.GIFT_RECEIVED);
  }

  public static RawNotificationData forOrderComplete(String orderNo, Long totalAmount) {
    Map<String, String> parameters = new HashMap<>();
    parameters.put("orderId", orderNo);
    parameters.put("totalAmount", String.valueOf(totalAmount));

    return new RawNotificationData(null, parameters, NotificationType.ORDER_COMPLETE);
  }

  public static RawNotificationData forOrderCanceled(
      Integer cancelAmount, String productName, Integer productQuantity) {
    Map<String, String> parameters = new HashMap<>();
    parameters.put("cancelAmount", cancelAmount.toString());
    parameters.put("productName", productName);
    parameters.put("productQuantity", productQuantity.toString());

    return new RawNotificationData(null, parameters, NotificationType.ORDER_CANCELED);
  }
}

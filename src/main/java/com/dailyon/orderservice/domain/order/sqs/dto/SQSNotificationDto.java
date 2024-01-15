package com.dailyon.orderservice.domain.order.sqs.dto;

import lombok.*;

import java.util.Collections;
import java.util.List;

@Getter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SQSNotificationDto {
  List<Long> whoToNotify; // if null, 전체유저에게 발송

  RawNotificationData rawNotificationData;

  public static SQSNotificationDto create(RawNotificationData rawNotificationData) {
    return SQSNotificationDto.builder().rawNotificationData(rawNotificationData).build();
  }

  public static SQSNotificationDto of(Long whoToNotifty, RawNotificationData rawNotificationData) {
    return new SQSNotificationDto(Collections.singletonList(whoToNotifty), rawNotificationData);
  }

  public static SQSNotificationDto of(
      List<Long> whoToNotifty, RawNotificationData rawNotificationData) {
    return new SQSNotificationDto(whoToNotifty, rawNotificationData);
  }
}

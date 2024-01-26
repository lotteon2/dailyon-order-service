package com.dailyon.orderservice.domain.order.message.sqs;

import com.dailyon.orderservice.domain.order.message.sqs.dto.SQSNotificationDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.aws.messaging.core.QueueMessagingTemplate;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderSqsProducer {
  public static final String GIFT_RECEIVED_NOTIFICATION_QUEUE = "gift-received-notification-queue";
  public static final String ORDER_COMPLETE_NOTIFICATION_QUEUE =
      "order-complete-notification-queue";
  public static final String ORDER_SHIPPED_NOTIFICATION_QUEUE = "order-shipped-notification-queue";
  public static final String ORDER_CANCELED_NOTIFICATION_QUEUE =
      "order-canceled-notification-queue";
  private final QueueMessagingTemplate sqsTemplate;
  private final ObjectMapper objectMapper;

  public void produce(String queueName, SQSNotificationDto sqsNotificationDto) {
    try {
      String jsonMessage = objectMapper.writeValueAsString(sqsNotificationDto);
      Message<String> message = MessageBuilder.withPayload(jsonMessage).build();
      sqsTemplate.send(queueName, message);
    } catch (Exception e) {
      log.error(e.getMessage());
    }
  }
}

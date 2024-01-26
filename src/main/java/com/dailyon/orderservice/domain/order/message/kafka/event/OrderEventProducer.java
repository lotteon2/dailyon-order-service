package com.dailyon.orderservice.domain.order.message.kafka.event;

import com.dailyon.orderservice.domain.order.message.kafka.event.dto.RefundDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dailyon.domain.common.KafkaTopic;
import dailyon.domain.order.kafka.OrderDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderEventProducer {

  private final KafkaTemplate<String, String> kafkaTemplate;
  private final ObjectMapper objectMapper;

  public void createRefund(String orderDetailNo, RefundDTO refundDTO) {
    log.info("create-refund success -> orderDetailNo {}", orderDetailNo);
    try {
      String data = objectMapper.writeValueAsString(refundDTO);
      kafkaTemplate.send("create-refund", data);
    } catch (JsonProcessingException e) {
      e.printStackTrace();
    }
  }

  public void orderCreated(OrderDTO orderDTO) {
    log.info("order-created -> orderId {}", orderDTO.getOrderNo());
    try {
      System.out.println(orderDTO.getReferralCode());
      kafkaTemplate.send(KafkaTopic.CREATE_ORDER, objectMapper.writeValueAsString(orderDTO));
    } catch (JsonProcessingException e) {
      e.printStackTrace();
    }
  }
}

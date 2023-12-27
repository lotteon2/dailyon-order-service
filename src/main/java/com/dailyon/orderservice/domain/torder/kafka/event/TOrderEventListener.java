package com.dailyon.orderservice.domain.torder.kafka.event;

import com.dailyon.orderservice.domain.torder.service.TOrderService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dailyon.domain.order.kafka.OrderDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TOrderEventListener {
  private final ObjectMapper objectMapper;
  private final TOrderService tOrderService;

  @KafkaListener(topics = "cancel-order")
  public void cancel(String message, Acknowledgment ack) {
    OrderDTO orderDTO = null;
    try {
      orderDTO = objectMapper.readValue(message, OrderDTO.class);
      tOrderService.modifyTOrder(orderDTO.getOrderNo(), orderDTO.getOrderEvent());
      ack.acknowledge();
    } catch (JsonProcessingException e) {
      e.printStackTrace();
    }
  }
}

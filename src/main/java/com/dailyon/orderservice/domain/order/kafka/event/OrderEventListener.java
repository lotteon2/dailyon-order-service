package com.dailyon.orderservice.domain.order.kafka.event;

import com.dailyon.orderservice.domain.order.facade.OrderFacade;
import com.dailyon.orderservice.domain.torder.kafka.event.dto.OrderDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class OrderEventListener {
  private final ObjectMapper objectMapper;
  private final OrderFacade orderFacade;

  @KafkaListener(topics = "payment-approved")
  public void saveOrder(String message, Acknowledgment ack) {
    try {
      OrderDTO orderDTO = objectMapper.readValue(message, OrderDTO.class);
      orderFacade.orderCreate(orderDTO.getTOrder(), orderDTO.getOrderEvent());
      ack.acknowledge();
    } catch (JsonProcessingException e) {
      e.printStackTrace();
    }
  }
}

package com.dailyon.orderservice.domain.order.kafka.event;

import com.dailyon.orderservice.domain.order.facade.OrderFacade;
import com.dailyon.orderservice.domain.order.kafka.event.dto.ReviewDTO;
import com.dailyon.orderservice.domain.order.service.OrderService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dailyon.domain.order.kafka.OrderDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class OrderEventListener {
  private final ObjectMapper objectMapper;
  private final OrderFacade orderFacade;
  private final OrderService orderService;

  @KafkaListener(topics = "approve-payment")
  public void saveOrder(String message, Acknowledgment ack) {
    OrderDTO orderDTO = null;
    try {
      orderDTO = objectMapper.readValue(message, OrderDTO.class);
      orderFacade.orderCreate(orderDTO.getOrderNo(), orderDTO.getOrderEvent());
      ack.acknowledge();
    } catch (JsonProcessingException e) {
      e.printStackTrace();
    }
  }

  @KafkaListener(topics = "create-review")
  public void modifyOrderDetail(String message, Acknowledgment ack) {
    try {
      ReviewDTO reviewDTO = objectMapper.readValue(message, ReviewDTO.class);
      orderService.modifyOrderDetail(reviewDTO.getOrderDetailNo(), reviewDTO.getMemberId());
      ack.acknowledge();
    } catch (JsonProcessingException e) {
      e.printStackTrace();
    }
  }
}

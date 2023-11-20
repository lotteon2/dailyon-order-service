package com.dailyon.orderservice.domain.order.implement;

import com.dailyon.orderservice.domain.order.entity.Order;
import com.dailyon.orderservice.domain.order.exception.OrderNotFoundException;
import com.dailyon.orderservice.domain.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class OrderReader {
  private final OrderRepository orderRepository;

  public Order read(Long orderId) {
    return orderRepository.findById(orderId).orElseThrow(OrderNotFoundException::new);
  }
}

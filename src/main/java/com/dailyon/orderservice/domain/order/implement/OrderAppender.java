package com.dailyon.orderservice.domain.order.implement;

import com.dailyon.orderservice.domain.order.entity.Order;
import com.dailyon.orderservice.domain.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class OrderAppender {
  private final OrderRepository orderRepository;

  public Order append(Order order) {
    Order savedOrder = orderRepository.save(order);
    return savedOrder;
  }
}

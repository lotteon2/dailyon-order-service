package com.dailyon.orderservice.domain.order.implement;

import com.dailyon.orderservice.domain.order.entity.Order;
import com.dailyon.orderservice.domain.order.entity.OrderDetail;
import com.dailyon.orderservice.domain.order.repository.OrderDetailRepository;
import com.dailyon.orderservice.domain.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@RequiredArgsConstructor
@Component
public class OrderAppender {
  private final OrderRepository orderRepository;
  private final OrderDetailRepository orderDetailRepository;

  public Order append(Order order, List<OrderDetail> orderDetails) {
    orderRepository.save(order);
    orderDetailRepository.saveAll(orderDetails);
    order.add(orderDetails);
    return order;
  }
}

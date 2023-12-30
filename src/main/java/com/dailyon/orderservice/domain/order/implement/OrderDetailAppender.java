package com.dailyon.orderservice.domain.order.implement;

import com.dailyon.orderservice.domain.order.entity.OrderDetail;
import com.dailyon.orderservice.domain.order.repository.OrderDetailRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@RequiredArgsConstructor
@Component
public class OrderDetailAppender {
  private final OrderDetailRepository orderDetailRepository;

  public List<OrderDetail> append(List<OrderDetail> orderDetails) {
    List<OrderDetail> savedOrderDetails = orderDetailRepository.saveAll(orderDetails);
    return savedOrderDetails;
  }
}

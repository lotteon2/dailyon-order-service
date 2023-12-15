package com.dailyon.orderservice.domain.order.implement;

import com.dailyon.orderservice.domain.order.entity.Order;
import com.dailyon.orderservice.domain.order.entity.OrderDetail;
import com.dailyon.orderservice.domain.order.repository.OrderDetailRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class OrderManager {
  private final OrderDetailRepository orderDetailRepository;

  public void cancelDetail(OrderDetail orderDetail) {
    orderDetail.cancel();
  }
}

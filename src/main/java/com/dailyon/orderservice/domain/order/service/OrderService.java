package com.dailyon.orderservice.domain.order.service;

import com.dailyon.orderservice.domain.order.entity.Order;
import com.dailyon.orderservice.domain.order.entity.OrderDetail;
import com.dailyon.orderservice.domain.order.implement.OrderAppender;
import com.dailyon.orderservice.domain.order.implement.OrderDetailAppender;
import com.dailyon.orderservice.domain.torder.entity.TOrder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderService {
  private final OrderAppender orderAppender;
  private final OrderDetailAppender orderDetailAppender;

  @Transactional
  public Order createOrder(TOrder tOrder) {
    Order savedOrder = orderAppender.append(tOrder.toEntity());
    List<OrderDetail> orderDetails = tOrder.createOrderDetails(savedOrder);
    List<OrderDetail> savedOrderDetails = orderDetailAppender.append(orderDetails);
    savedOrder.add(savedOrderDetails);
    return savedOrder;
  }
}

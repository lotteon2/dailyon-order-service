package com.dailyon.orderservice.domain.order.implement;

import com.dailyon.orderservice.common.exception.AuthorizationException;
import com.dailyon.orderservice.domain.order.entity.Order;
import com.dailyon.orderservice.domain.order.entity.OrderDetail;
import com.dailyon.orderservice.domain.order.exception.OrderNotFoundException;
import com.dailyon.orderservice.domain.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;

@RequiredArgsConstructor
@Component
public class OrderReader {
  private final OrderRepository orderRepository;

  public Order read(String orderNo) {
    return orderRepository.findByOrderNo(orderNo).orElseThrow(OrderNotFoundException::new);
  }

  public Page<Order> read(Pageable pageable, Long memberId) {
    return orderRepository.findAllWithPaging(pageable, memberId);
  }

  public List<OrderDetail> readDetails(String orderNo, Long memberId) {
    Order order = orderRepository.findByOrderNo(orderNo).orElseThrow(OrderNotFoundException::new);
    checkAuthorization(order, memberId);
    return order.getOrderDetails();
  }

  public void checkAuthorization(Order order, Long memberId) {
    if (order.getMemberId() != memberId) {
      throw new AuthorizationException();
    }
  }
}

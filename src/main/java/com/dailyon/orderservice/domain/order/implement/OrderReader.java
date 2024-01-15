package com.dailyon.orderservice.domain.order.implement;

import com.dailyon.orderservice.common.exception.AuthorizationException;
import com.dailyon.orderservice.domain.order.entity.Order;
import com.dailyon.orderservice.domain.order.entity.OrderDetail;
import com.dailyon.orderservice.domain.order.exception.OrderDetailNotFoundException;
import com.dailyon.orderservice.domain.order.exception.OrderNotFoundException;
import com.dailyon.orderservice.domain.order.repository.OrderDetailRepository;
import com.dailyon.orderservice.domain.order.repository.OrderRepository;
import dailyon.domain.order.clients.ProductRankResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;

@RequiredArgsConstructor
@Component
public class OrderReader {
  private final OrderRepository orderRepository;
  private final OrderDetailRepository orderDetailRepository;

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

  public OrderDetail readDetail(String orderDetailNo, Long memberId) {
    OrderDetail orderDetail =
        orderDetailRepository
            .findByNoFetch(orderDetailNo)
            .orElseThrow(OrderDetailNotFoundException::new);
    checkAuthorization(orderDetail.getOrder(), memberId);
    return orderDetail;
  }

  public List<ProductRankResponse> readMostSoldProducts(int limit) {
    return orderDetailRepository.findProductIdsOrderByCount(limit);
  }

  private void checkAuthorization(Order order, Long memberId) {
    if (order.getMemberId() != memberId) {
      throw new AuthorizationException();
    }
  }
}

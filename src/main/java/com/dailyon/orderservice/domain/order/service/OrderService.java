package com.dailyon.orderservice.domain.order.service;

import com.dailyon.orderservice.domain.order.entity.Order;
import com.dailyon.orderservice.domain.order.entity.OrderDetail;
import com.dailyon.orderservice.domain.order.entity.enums.OrderType;
import com.dailyon.orderservice.domain.order.implement.OrderAppender;
import com.dailyon.orderservice.domain.order.implement.OrderDetailAppender;
import com.dailyon.orderservice.domain.order.implement.OrderManager;
import com.dailyon.orderservice.domain.order.implement.OrderReader;
import com.dailyon.orderservice.domain.order.dynamo.document.TOrder;
import dailyon.domain.order.clients.ProductRankResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.dailyon.orderservice.common.utils.OrderConstants.BEST_SELLER_LIMIT;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderService {
  private final OrderAppender orderAppender;
  private final OrderDetailAppender orderDetailAppender;
  private final OrderReader orderReader;
  private final OrderManager orderManager;

  @Transactional
  public Order createOrder(TOrder tOrder) {
    Order savedOrder = orderAppender.append(tOrder.toEntity());
    List<OrderDetail> orderDetails = tOrder.createOrderDetails(savedOrder);
    List<OrderDetail> savedOrderDetails = orderDetailAppender.append(orderDetails);
    savedOrder.add(savedOrderDetails);
    return savedOrder;
  }

  public Page<Order> getOrders(Pageable pageable, OrderType type, Long memberId) {
    return orderReader.read(pageable, type, memberId);
  }

  public List<OrderDetail> getOrderDetails(String orderNo, Long memberId) {
    return orderReader.readDetails(orderNo, memberId);
  }

  @Transactional
  public OrderDetail cancelOrderDetail(String orderDetailNo, Long memberId) {
    OrderDetail orderDetail = orderReader.readDetail(orderDetailNo, memberId);
    orderManager.cancelDetail(orderDetail, memberId);
    return orderDetail;
  }

  @Transactional
  public void modifyOrderDetail(String orderDetailNo, Long memberId) {
    OrderDetail orderDetail = orderReader.readDetail(orderDetailNo, memberId);
    orderManager.changeReviewCheck(orderDetail);
  }

  public List<ProductRankResponse> getMostSoldProducts() {
    return orderReader.readMostSoldProducts(BEST_SELLER_LIMIT);
  }
}

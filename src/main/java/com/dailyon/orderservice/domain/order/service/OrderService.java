package com.dailyon.orderservice.domain.order.service;

import com.dailyon.orderservice.common.utils.OrderNoGenerator;
import com.dailyon.orderservice.domain.order.dynamo.document.TDelivery;
import com.dailyon.orderservice.domain.order.dynamo.document.TOrder;
import com.dailyon.orderservice.domain.order.entity.Order;
import com.dailyon.orderservice.domain.order.entity.OrderDetail;
import com.dailyon.orderservice.domain.order.entity.enums.OrderType;
import com.dailyon.orderservice.domain.order.implement.*;
import com.dailyon.orderservice.domain.order.service.request.TOrderCommand;
import dailyon.domain.order.clients.ProductRankResponse;
import dailyon.domain.order.kafka.enums.OrderEvent;
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
  private final TOrderAppender tOrderAppender;
  private final TOrderReader tOrderReader;
  private final TOrderManager tOrderManager;

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

  public TOrder createTOrder(TOrderCommand.RegisterTOrder requestOrder, Long memberId) {
    String orderNo = OrderNoGenerator.generate(memberId);
    TDelivery tDelivery = requestOrder.createTDelivery(orderNo);
    TOrder order = requestOrder.createOrder(orderNo, memberId, tDelivery);
    return tOrderAppender.append(order);
  }

  public TOrder getTOrder(String orderNo) {
    return tOrderReader.read(orderNo);
  }

  public TOrder modifyTOrder(String orderNo, OrderEvent event) {
    TOrder tOrder = tOrderReader.read(orderNo);
    TOrder changedTOrder = tOrderManager.changeStatus(tOrder, event);
    return changedTOrder;
  }

  public void deleteTOrder(String orderNo) {
    tOrderManager.delete(orderNo);
  }
}

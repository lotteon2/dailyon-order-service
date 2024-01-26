package com.dailyon.orderservice.domain.order.service;

import com.dailyon.orderservice.common.utils.OrderNoGenerator;
import com.dailyon.orderservice.domain.order.dynamo.document.TDelivery;
import com.dailyon.orderservice.domain.order.dynamo.document.TOrder;
import com.dailyon.orderservice.domain.order.implement.TOrderAppender;
import com.dailyon.orderservice.domain.order.implement.TOrderManager;
import com.dailyon.orderservice.domain.order.implement.TOrderReader;
import dailyon.domain.order.kafka.enums.OrderEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TOrderService {
  private final TOrderAppender tOrderAppender;
  private final TOrderReader tOrderReader;
  private final TOrderManager tOrderManager;

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

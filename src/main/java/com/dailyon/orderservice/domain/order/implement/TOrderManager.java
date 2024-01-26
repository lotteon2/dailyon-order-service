package com.dailyon.orderservice.domain.order.implement;

import com.dailyon.orderservice.domain.order.dynamo.document.TOrder;
import com.dailyon.orderservice.domain.order.dynamo.repository.OrderDynamoRepository;
import dailyon.domain.order.kafka.enums.OrderEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class TOrderManager {
  private final OrderDynamoRepository orderDynamoRepository;

  public TOrder changeStatus(TOrder tOrder, OrderEvent orderEvent) {
    tOrder.changeStatus(orderEvent);
    TOrder savedTOrder = orderDynamoRepository.save(tOrder);
    return savedTOrder;
  }

  public void delete(String orderId) {
    orderDynamoRepository.deleteById(orderId);
  }
}

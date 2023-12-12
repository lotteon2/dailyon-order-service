package com.dailyon.orderservice.domain.torder.implement;

import com.dailyon.orderservice.domain.torder.entity.TOrder;
import com.dailyon.orderservice.domain.torder.kafka.event.dto.enums.OrderEvent;
import com.dailyon.orderservice.domain.torder.repository.OrderDynamoRepository;
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

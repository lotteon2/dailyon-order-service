package com.dailyon.orderservice.domain.order.implement;

import com.dailyon.orderservice.domain.order.exception.OrderNotFoundException;
import com.dailyon.orderservice.domain.order.dynamo.document.TOrder;
import com.dailyon.orderservice.domain.order.dynamo.repository.OrderDynamoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class TOrderReader {
  private final OrderDynamoRepository orderDynamoRepository;

  public TOrder read(String orderId) {
    return orderDynamoRepository.findById(orderId).orElseThrow(OrderNotFoundException::new);
  }
}

package com.dailyon.orderservice.domain.order.implement;

import com.dailyon.orderservice.domain.order.dynamo.document.TOrder;
import com.dailyon.orderservice.domain.order.dynamo.repository.OrderDynamoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class TOrderAppender {
  private final OrderDynamoRepository orderDynamoRepository;

  public TOrder append(TOrder order) {
    return orderDynamoRepository.save(order);
  }
}

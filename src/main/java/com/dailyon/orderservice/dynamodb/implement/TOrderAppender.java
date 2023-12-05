package com.dailyon.orderservice.dynamodb.implement;

import com.dailyon.orderservice.dynamodb.entity.TOrder;
import com.dailyon.orderservice.dynamodb.repository.OrderDynamoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class TOrderAppender {
  private final OrderDynamoRepository orderDynamoRepository;

  public TOrder append(TOrder tOrder) {
    TOrder savedOrder = orderDynamoRepository.save(tOrder);
    return savedOrder;
  }
}

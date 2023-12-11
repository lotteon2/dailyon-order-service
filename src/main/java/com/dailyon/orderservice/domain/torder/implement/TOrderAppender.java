package com.dailyon.orderservice.domain.torder.implement;

import com.dailyon.orderservice.domain.torder.entity.TOrder;
import com.dailyon.orderservice.domain.torder.repository.OrderDynamoRepository;
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

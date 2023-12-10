package com.dailyon.orderservice.domain.torder.service;

import com.dailyon.orderservice.common.utils.OrderNoGenerator;
import com.dailyon.orderservice.domain.torder.entity.TOrder;
import com.dailyon.orderservice.domain.torder.implement.TOrderAppender;
import com.dailyon.orderservice.domain.torder.service.request.TOrderServiceRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TOrderService {
  private final TOrderAppender tOrderAppender;

  public TOrder createTOrder(TOrderServiceRequest request, Long memberId) {
    String orderId = OrderNoGenerator.generate(memberId);
    TOrder tOrder = request.createOrder(orderId, memberId);
    TOrder savedOrder = tOrderAppender.append(tOrder);
    return savedOrder;
  }
}

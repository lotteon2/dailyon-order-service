package com.dailyon.orderservice.domain.torder.service;

import com.dailyon.orderservice.common.utils.OrderNoGenerator;
import com.dailyon.orderservice.domain.torder.entity.TOrder;
import com.dailyon.orderservice.domain.torder.implement.TOrderAppender;
import com.dailyon.orderservice.domain.torder.implement.TOrderManager;
import com.dailyon.orderservice.domain.torder.implement.TOrderReader;
import com.dailyon.orderservice.domain.torder.kafka.event.dto.enums.OrderEvent;
import com.dailyon.orderservice.domain.torder.service.request.TOrderServiceRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TOrderService {
  private final TOrderAppender tOrderAppender;
  private final TOrderReader tOrderReader;
  private final TOrderManager tOrderManager;

  public TOrder createTOrder(TOrderServiceRequest request, Long memberId) {
    String orderId = OrderNoGenerator.generate(memberId);
    TOrder tOrder = request.createOrder(orderId, memberId);
    TOrder savedOrder = tOrderAppender.append(tOrder);
    return savedOrder;
  }

  public TOrder getTOrder(String orderId) {
    return tOrderReader.read(orderId);
  }

  public TOrder modifyTOrder(String orderId, OrderEvent event) {
    TOrder tOrder = tOrderReader.read(orderId);
    TOrder changedTOrder = tOrderManager.changeStatus(tOrder, event);
    return changedTOrder;
  }

  public void deleteTOrder(String orderId) {
    tOrderManager.delete(orderId);
  }
}

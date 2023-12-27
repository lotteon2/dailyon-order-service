package com.dailyon.orderservice.domain.torder.service;

import com.dailyon.orderservice.common.utils.OrderNoGenerator;
import com.dailyon.orderservice.domain.torder.entity.TDelivery;
import com.dailyon.orderservice.domain.torder.entity.TOrder;
import com.dailyon.orderservice.domain.torder.implement.TOrderAppender;
import com.dailyon.orderservice.domain.torder.implement.TOrderManager;
import com.dailyon.orderservice.domain.torder.implement.TOrderReader;
import com.dailyon.orderservice.domain.torder.kafka.event.dto.enums.OrderEvent;
import com.dailyon.orderservice.domain.torder.service.request.TOrderCommand;
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
    TOrder savedOrder = tOrderAppender.append(requestOrder, orderNo, memberId, tDelivery);
    return savedOrder;
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

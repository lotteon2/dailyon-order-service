package com.dailyon.orderservice.domain.order.facade;

import com.dailyon.orderservice.domain.order.service.OrderService;
import com.dailyon.orderservice.domain.torder.entity.TOrder;
import com.dailyon.orderservice.domain.torder.kafka.event.dto.enums.OrderEvent;
import com.dailyon.orderservice.domain.torder.service.TOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderFacade {
  private final OrderService orderService;
  private final TOrderService tOrderService;

  public String orderCreate(TOrder tOrder, OrderEvent event) {
    tOrderService.modifyTOrder(tOrder.getId(), event);
    return orderService.createOrder(tOrder).getId();
  }
}

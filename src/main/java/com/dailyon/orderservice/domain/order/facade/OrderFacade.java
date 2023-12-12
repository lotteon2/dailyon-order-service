package com.dailyon.orderservice.domain.order.facade;

import com.dailyon.orderservice.domain.delivery.service.DeliveryService;
import com.dailyon.orderservice.domain.delivery.service.request.DeliveryServiceRequest;
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
  private final DeliveryService deliveryService;

  public String orderCreate(TOrder tOrder, OrderEvent event) {
    orderService.createOrder(tOrder);
    deliveryService.createDelivery(DeliveryServiceRequest.from(tOrder.getDelivery()));
    tOrderService.deleteTOrder(tOrder.getId());
    return tOrder.getId();
  }
}

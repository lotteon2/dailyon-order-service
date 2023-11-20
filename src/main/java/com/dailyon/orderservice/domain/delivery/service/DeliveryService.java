package com.dailyon.orderservice.domain.delivery.service;

import com.dailyon.orderservice.domain.delivery.api.request.DeliveryCreateRequest;
import com.dailyon.orderservice.domain.delivery.entity.Delivery;
import com.dailyon.orderservice.domain.delivery.implement.DeliveryAppender;
import com.dailyon.orderservice.domain.order.entity.Order;
import com.dailyon.orderservice.domain.order.implement.OrderReader;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class DeliveryService {
  private final OrderReader orderReader;
  private final DeliveryAppender deliveryAppender;

  @Transactional
  public void createDelivery(DeliveryCreateRequest request) {
    Order order = orderReader.read(request.getOrderId());
    Delivery delivery = request.toEntity();
    deliveryAppender.append(delivery, order);
  }
}

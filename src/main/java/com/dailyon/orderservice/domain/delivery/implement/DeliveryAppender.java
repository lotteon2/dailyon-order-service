package com.dailyon.orderservice.domain.delivery.implement;

import com.dailyon.orderservice.domain.delivery.entity.Delivery;
import com.dailyon.orderservice.domain.delivery.repository.DeliveryRepository;
import com.dailyon.orderservice.domain.order.entity.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class DeliveryAppender {
  private final DeliveryRepository deliveryRepository;

  public Long append(Delivery delivery, Order order) {
    Delivery newDelivery = delivery.toBuilder().order(order).build();
    return deliveryRepository.save(newDelivery).getId();
  }
}

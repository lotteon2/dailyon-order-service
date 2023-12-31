package com.dailyon.orderservice.domain.delivery.implement;

import com.dailyon.orderservice.domain.delivery.entity.Delivery;
import com.dailyon.orderservice.domain.delivery.exception.DeliveryNotFoundException;
import com.dailyon.orderservice.domain.delivery.repository.DeliveryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class DeliveryReader {
  private final DeliveryRepository deliveryRepository;

  public Delivery read(String orderNo) {
    return deliveryRepository.findByOrderNo(orderNo).orElseThrow(DeliveryNotFoundException::new);
  }
}

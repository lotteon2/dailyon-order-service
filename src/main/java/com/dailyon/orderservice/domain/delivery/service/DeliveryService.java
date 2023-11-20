package com.dailyon.orderservice.domain.delivery.service;

import com.dailyon.orderservice.domain.delivery.repository.DeliveryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DeliveryService {
  private final DeliveryRepository deliveryRepository;
}

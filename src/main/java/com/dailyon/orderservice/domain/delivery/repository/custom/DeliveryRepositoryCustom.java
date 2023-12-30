package com.dailyon.orderservice.domain.delivery.repository.custom;

import com.dailyon.orderservice.domain.delivery.entity.Delivery;

import java.util.Optional;

public interface DeliveryRepositoryCustom {
  Optional<Delivery> findByOrderNo(String orderNo);
}

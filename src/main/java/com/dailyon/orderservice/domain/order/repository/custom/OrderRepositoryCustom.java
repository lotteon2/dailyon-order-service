package com.dailyon.orderservice.domain.order.repository.custom;

import com.dailyon.orderservice.domain.order.entity.Order;

import java.util.Optional;

public interface OrderRepositoryCustom {
  Optional<Order> findByOrderNo(String orderNo);
}

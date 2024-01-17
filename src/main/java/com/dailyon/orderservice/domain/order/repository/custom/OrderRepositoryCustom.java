package com.dailyon.orderservice.domain.order.repository.custom;

import com.dailyon.orderservice.domain.order.entity.Order;
import com.dailyon.orderservice.domain.order.entity.enums.OrderType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface OrderRepositoryCustom {
  Optional<Order> findByOrderNo(String orderNo);

  Page<Order> findAllWithPaging(Pageable pageable, OrderType type, String role, Long memberId);
}

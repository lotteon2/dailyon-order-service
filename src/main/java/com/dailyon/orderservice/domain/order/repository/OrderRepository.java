package com.dailyon.orderservice.domain.order.repository;

import com.dailyon.orderservice.domain.order.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {}
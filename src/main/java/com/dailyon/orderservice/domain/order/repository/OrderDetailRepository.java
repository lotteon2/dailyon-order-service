package com.dailyon.orderservice.domain.order.repository;

import com.dailyon.orderservice.domain.order.entity.OrderDetail;
import com.dailyon.orderservice.domain.order.repository.custom.OrderDetailRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderDetailRepository
    extends JpaRepository<OrderDetail, String>, OrderDetailRepositoryCustom {}

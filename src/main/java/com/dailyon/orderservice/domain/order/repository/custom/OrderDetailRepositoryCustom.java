package com.dailyon.orderservice.domain.order.repository.custom;

import com.dailyon.orderservice.domain.order.entity.OrderDetail;

import java.util.Optional;

public interface OrderDetailRepositoryCustom {
    Optional<OrderDetail> findByNoFetch(String orderDetailNo);
}

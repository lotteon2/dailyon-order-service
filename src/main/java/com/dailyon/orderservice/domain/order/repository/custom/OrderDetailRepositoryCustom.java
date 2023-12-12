package com.dailyon.orderservice.domain.order.repository.custom;

import com.dailyon.orderservice.domain.order.entity.OrderDetail;

import java.util.List;

public interface OrderDetailRepositoryCustom {
  List<OrderDetail> findByOrderNo(String orderNo);
}

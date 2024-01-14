package com.dailyon.orderservice.domain.order.repository.custom;

import com.dailyon.orderservice.domain.order.entity.OrderDetail;

import java.util.List;
import java.util.Optional;

public interface OrderDetailRepositoryCustom {
  Optional<OrderDetail> findByNoFetch(String orderDetailNo);
  List<Long> findProductIdsOrderByCount(int limit);
}

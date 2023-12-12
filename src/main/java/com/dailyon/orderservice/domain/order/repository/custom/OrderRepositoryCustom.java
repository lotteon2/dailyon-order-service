package com.dailyon.orderservice.domain.order.repository.custom;

import com.dailyon.orderservice.domain.order.entity.Order;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface OrderRepositoryCustom {
  Optional<Order> findByOrderNo(String orderNo);

  List<Order> findAllWithPaging(Pageable pageable, Long memberId);

  Long getTotalPageCount(Long memberId);
}

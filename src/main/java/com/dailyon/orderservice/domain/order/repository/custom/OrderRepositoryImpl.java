package com.dailyon.orderservice.domain.order.repository.custom;

import com.dailyon.orderservice.domain.order.entity.Order;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

import static com.dailyon.orderservice.domain.order.entity.QOrder.order;

@RequiredArgsConstructor
public class OrderRepositoryImpl implements OrderRepositoryCustom {
  private final JPAQueryFactory queryFactory;

  @Override
  public Optional<Order> findByOrderNo(String orderNo) {
    return Optional.ofNullable(
        queryFactory.selectFrom(order).where(order.orderNo.eq(orderNo)).fetchOne());
  }
}

package com.dailyon.orderservice.domain.order.repository.custom;

import com.dailyon.orderservice.domain.order.entity.OrderDetail;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

import static com.dailyon.orderservice.domain.order.entity.QOrderDetail.orderDetail;

@RequiredArgsConstructor
public class OrderDetailRepositoryImpl implements OrderDetailRepositoryCustom {
  private final JPAQueryFactory queryFactory;

  @Override
  public Optional<OrderDetail> findByNoFetch(String orderDetailNo) {
    return Optional.ofNullable(
        queryFactory
            .selectFrom(orderDetail)
            .join(orderDetail.order)
            .fetchJoin()
            .where(orderDetail.orderDetailNo.eq(orderDetailNo))
            .fetchOne());
  }
}

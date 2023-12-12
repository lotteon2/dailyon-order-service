package com.dailyon.orderservice.domain.order.repository.custom;

import com.dailyon.orderservice.domain.order.entity.OrderDetail;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.dailyon.orderservice.domain.order.entity.QOrderDetail.orderDetail;

@RequiredArgsConstructor
public class OrderDetailRepositoryImpl implements OrderDetailRepositoryCustom {
  private final JPAQueryFactory queryFactory;

  @Override
  public List<OrderDetail> findByOrderNo(String orderNo) {
    return queryFactory
        .selectFrom(orderDetail)
        .where(orderDetail.orderNo.eq(orderNo))
        .fetch();
  }
}

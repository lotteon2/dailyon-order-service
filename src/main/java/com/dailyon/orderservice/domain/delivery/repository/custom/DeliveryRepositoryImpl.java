package com.dailyon.orderservice.domain.delivery.repository.custom;

import com.dailyon.orderservice.domain.delivery.entity.Delivery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

import static com.dailyon.orderservice.domain.delivery.entity.QDelivery.delivery;

@RequiredArgsConstructor
public class DeliveryRepositoryImpl implements DeliveryRepositoryCustom {
  private final JPAQueryFactory queryFactory;

  @Override
  public Optional<Delivery> findByOrderId(String orderId) {
    return Optional.ofNullable(
        queryFactory.selectFrom(delivery).where(delivery.order.orderId.eq(orderId)).fetchOne());
  }
}

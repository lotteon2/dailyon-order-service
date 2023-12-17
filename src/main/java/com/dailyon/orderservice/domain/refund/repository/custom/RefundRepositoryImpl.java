package com.dailyon.orderservice.domain.refund.repository.custom;

import com.dailyon.orderservice.domain.refund.entity.Refund;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

import static com.dailyon.orderservice.domain.refund.entity.QRefund.refund;

@RequiredArgsConstructor
public class RefundRepositoryImpl implements RefundRepositoryCustom {
  private final JPAQueryFactory queryFactory;

  @Override
  public List<Refund> findByOrderNo(String orderNo) {
    return queryFactory.selectFrom(refund).where(refund.order.orderNo.eq(orderNo)).fetch();
  }

  @Override
  public boolean existByOrderDetailNo(String orderDetailNo) {
    return queryFactory
                .select(refund.count())
                .from(refund)
                .where(refund.orderDetail.orderDetailNo.eq(orderDetailNo))
                .fetchOne()
            == 1
        ? true
        : false;
  }

  @Override
  public int getTotalRefundedPoints(String orderNo) {
    return Optional.ofNullable(
            queryFactory
                .select(refund.points.sum())
                .from(refund)
                .where(refund.order.orderNo.eq(orderNo))
                .fetchOne())
        .orElse(0);
  }
}

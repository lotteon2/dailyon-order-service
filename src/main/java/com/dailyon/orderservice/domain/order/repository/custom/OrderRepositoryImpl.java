package com.dailyon.orderservice.domain.order.repository.custom;

import com.dailyon.orderservice.domain.order.entity.Order;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
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

  @Override
  public List<Order> findAllWithPaging(Pageable pageable, Long memberId) {

    List<Long> ids =
        queryFactory
            .select(order.id)
            .from(order)
            .where(order.memberId.eq(memberId))
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .orderBy(order.id.desc())
            .fetch();

    if (CollectionUtils.isEmpty(ids)) {
      return Collections.EMPTY_LIST;
    }

    List<Order> fetch =
        queryFactory.selectFrom(order).where(order.id.in(ids)).orderBy(order.id.desc()).fetch();

    return fetch;
  }

  @Override
  public Long getTotalPageCount(Long memberId) {
    return queryFactory
        .select(order.count())
        .from(order)
        .where(order.memberId.eq(memberId))
        .fetchOne();
  }
}

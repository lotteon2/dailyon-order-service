package com.dailyon.orderservice.domain.order.repository.custom;

import com.dailyon.orderservice.domain.order.entity.Order;
import com.dailyon.orderservice.domain.order.entity.enums.OrderType;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.dailyon.orderservice.domain.order.entity.QOrder.order;
import static com.dailyon.orderservice.domain.order.entity.enums.OrderType.CART;
import static com.dailyon.orderservice.domain.order.entity.enums.OrderType.SINGLE;

@RequiredArgsConstructor
public class OrderRepositoryImpl implements OrderRepositoryCustom {
  private final JPAQueryFactory queryFactory;

  @Override
  public Optional<Order> findByOrderNo(String orderNo) {
    return Optional.ofNullable(
        queryFactory.selectFrom(order).where(order.orderNo.eq(orderNo)).fetchOne());
  }

  @Override
  public Page<Order> findAllWithPaging(Pageable pageable, OrderType type, Long memberId) {

    List<Long> ids =
        queryFactory
            .select(order.id)
            .from(order)
            .where(getRoleCondition(memberId), eqType(type))
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .orderBy(order.createdAt.desc())
            .fetch();

    if (CollectionUtils.isEmpty(ids)) {
      return new PageImpl<>(Collections.EMPTY_LIST, pageable, 0);
    }

    List<Order> fetch =
        queryFactory.selectFrom(order).where(order.id.in(ids)).orderBy(order.id.desc()).fetch();

    return PageableExecutionUtils.getPage(fetch, pageable, () -> getTotalPageCount(type, memberId));
  }

  private Long getTotalPageCount(OrderType type, Long memberId) {
    return queryFactory
        .select(order.count())
        .from(order)
        .where(getRoleCondition(memberId), eqType(type))
        .fetchOne();
  }

  private BooleanExpression getRoleCondition(Long memberId) {
    return memberId == 0 ? null : order.memberId.eq(memberId);
  }

  private BooleanExpression eqType(OrderType type) {
    if (type == null) return null;
    return type == SINGLE || type == CART
        ? order.type.eq(SINGLE).or(order.type.eq(CART))
        : order.type.eq(type);
  }
}

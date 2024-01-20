package com.dailyon.orderservice.domain.delivery.repository.custom;

import com.dailyon.orderservice.domain.delivery.entity.Delivery;
import com.dailyon.orderservice.domain.delivery.entity.enums.DeliveryStatus;
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

import static com.dailyon.orderservice.domain.delivery.entity.QDelivery.delivery;

@RequiredArgsConstructor
public class DeliveryRepositoryImpl implements DeliveryRepositoryCustom {
  private final JPAQueryFactory queryFactory;

  @Override
  public Optional<Delivery> findByOrderNo(String orderNo) {
    return Optional.ofNullable(
        queryFactory.selectFrom(delivery).where(delivery.order.orderNo.eq(orderNo)).fetchOne());
  }

  @Override
  public Page<Delivery> findWithPagingInFetch(Pageable pageable, DeliveryStatus status) {
    List<Long> ids =
        queryFactory
            .select(delivery.id)
            .from(delivery)
            .where(eqStatus(status))
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .orderBy(delivery.id.desc())
            .fetch();

    if (CollectionUtils.isEmpty(ids)) {
      return new PageImpl<>(Collections.EMPTY_LIST, pageable, 0);
    }

    List<Delivery> fetch =
        queryFactory
            .selectFrom(delivery)
            .where(delivery.id.in(ids))
            .orderBy(delivery.id.desc())
            .fetch();

    return PageableExecutionUtils.getPage(fetch, pageable, () -> getTotalPageCount(status));
  }

  private Long getTotalPageCount(DeliveryStatus status) {
    return queryFactory
        .select(delivery.id.count())
        .from(delivery)
        .where(eqStatus(status))
        .fetchOne();
  }

  private BooleanExpression eqStatus(DeliveryStatus status) {
    if (status == null) return null;
    return delivery.status.eq(status);
  }
}

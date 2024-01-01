package com.dailyon.orderservice.domain.order.repository.custom;

import com.dailyon.orderservice.domain.order.entity.Gift;
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

import static com.dailyon.orderservice.domain.order.entity.QGift.gift;
import static com.dailyon.orderservice.domain.order.entity.QOrder.order;

@RequiredArgsConstructor
public class GiftRepositoryImpl implements GiftCustomRepository {
  private final JPAQueryFactory queryFactory;

  @Override
  public Optional<Gift> findByOrderNo(String orderNo) {
    return Optional.ofNullable(
        queryFactory.selectFrom(gift).where(gift.orderNo.eq(orderNo)).fetchOne());
  }

  @Override
  public Page<Gift> findByReceiverId(Long receiverId, Pageable pageable) {

    List<Long> ids =
        queryFactory
            .select(gift.id)
            .from(gift)
            .where(gift.receiverId.eq(receiverId))
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .orderBy(gift.id.desc())
            .fetch();

    if (CollectionUtils.isEmpty(ids)) {
      return new PageImpl<>(Collections.EMPTY_LIST, pageable, 0);
    }

    List<Gift> fetch =
        queryFactory.selectFrom(gift).where(gift.id.in(ids)).orderBy(gift.id.desc()).fetch();

    return PageableExecutionUtils.getPage(fetch, pageable, () -> getTotalPageCount(receiverId));
  }

  private Long getTotalPageCount(Long memberId) {
    return queryFactory
        .select(order.count())
        .from(order)
        .where(order.memberId.eq(memberId))
        .fetchOne();
  }
}

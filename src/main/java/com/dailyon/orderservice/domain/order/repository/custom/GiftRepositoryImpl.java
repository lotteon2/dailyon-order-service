package com.dailyon.orderservice.domain.order.repository.custom;

import com.dailyon.orderservice.domain.order.entity.Gift;
import com.querydsl.jpa.impl.JPAQuery;
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
        queryFactory.selectFrom(gift)
                .join(gift.order)
                .fetchJoin()
                .where(gift.id.in(ids))
                .orderBy(gift.id.desc()).fetch();

    JPAQuery<Long> query =
        queryFactory.select(gift.count()).from(gift).where(gift.receiverId.eq(receiverId));

    return PageableExecutionUtils.getPage(fetch, pageable, () -> query.fetchOne());
  }

  @Override
  public Page<Gift> findBySenderId(Long memberId, Pageable pageable) {
    List<Long> ids =
        queryFactory
            .select(gift.id)
            .from(gift)
            .innerJoin(gift.order)
            .where(gift.order.memberId.eq(memberId))
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .orderBy(gift.id.desc())
            .fetch();

    if (CollectionUtils.isEmpty(ids)) {
      return new PageImpl<>(Collections.EMPTY_LIST, pageable, 0);
    }

    List<Gift> fetch =
            queryFactory.selectFrom(gift)
                    .join(gift.order)
                    .fetchJoin()
                    .where(gift.id.in(ids))
                    .orderBy(gift.id.desc()).fetch();

    JPAQuery<Long> query =
        queryFactory
            .select(gift.count())
            .from(gift)
            .innerJoin(gift.order)
            .where(gift.order.memberId.eq(memberId));

    return PageableExecutionUtils.getPage(fetch, pageable, () -> query.fetchOne());
  }
}

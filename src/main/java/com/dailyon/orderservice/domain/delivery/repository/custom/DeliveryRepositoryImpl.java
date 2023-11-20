package com.dailyon.orderservice.domain.delivery.repository.custom;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class DeliveryRepositoryImpl implements DeliveryRepositoryCustom {
    private final JPAQueryFactory queryFactory;
}

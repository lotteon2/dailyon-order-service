package com.dailyon.orderservice.domain.order.repository;

import com.dailyon.orderservice.domain.order.entity.Gift;
import com.dailyon.orderservice.domain.order.repository.custom.GiftCustomRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GiftRepository extends JpaRepository<Gift, Long>, GiftCustomRepository {}

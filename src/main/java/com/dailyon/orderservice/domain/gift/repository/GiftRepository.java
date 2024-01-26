package com.dailyon.orderservice.domain.gift.repository;

import com.dailyon.orderservice.domain.gift.entity.Gift;
import com.dailyon.orderservice.domain.gift.repository.custom.GiftCustomRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GiftRepository extends JpaRepository<Gift, Long>, GiftCustomRepository {}

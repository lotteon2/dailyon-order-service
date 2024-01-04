package com.dailyon.orderservice.domain.order.repository.custom;

import com.dailyon.orderservice.domain.order.entity.Gift;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface GiftCustomRepository {
  Optional<Gift> findByOrderNo(String orderNo);

  Page<Gift> findByReceiverId(Long receiverId, Pageable pageable);

  Page<Gift> findBySenderId(Long memberId, Pageable pageable);
}

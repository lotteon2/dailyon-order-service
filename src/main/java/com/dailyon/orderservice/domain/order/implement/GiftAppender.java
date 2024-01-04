package com.dailyon.orderservice.domain.order.implement;

import com.dailyon.orderservice.domain.order.entity.Gift;
import com.dailyon.orderservice.domain.order.repository.GiftRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GiftAppender {
  private final GiftRepository giftRepository;

  public Gift append(Gift gift) {
    return giftRepository.save(gift);
  }
}

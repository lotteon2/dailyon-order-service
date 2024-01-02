package com.dailyon.orderservice.domain.order.implement;

import com.dailyon.orderservice.domain.order.entity.Gift;
import com.dailyon.orderservice.domain.order.exception.OrderNotFoundException;
import com.dailyon.orderservice.domain.order.repository.GiftRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GiftReader {
  private final GiftRepository giftRepository;

  public Gift read(String orderNo) {
    return giftRepository.findByOrderNo(orderNo).orElseThrow(OrderNotFoundException::new);
  }

  public Page<Gift> readByReceiver(Long receiverId, Pageable pageable) {
    return giftRepository.findByReceiverId(receiverId, pageable);
  }

  public Page<Gift> readBySender(Long memberId, Pageable pageable) {
    return giftRepository.findBySenderId(memberId, pageable);
  }
}

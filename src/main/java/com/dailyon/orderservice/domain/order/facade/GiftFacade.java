package com.dailyon.orderservice.domain.order.facade;

import com.dailyon.orderservice.domain.order.entity.Gift;
import com.dailyon.orderservice.domain.order.facade.response.GiftPageResponse;
import com.dailyon.orderservice.domain.order.service.GiftService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GiftFacade {
  private final GiftService giftService;

  public GiftPageResponse getReceiverGifts(Long receiverId, Pageable pageable) {
    Page<Gift> page = giftService.getGiftsByReceiver(receiverId, pageable);
    return GiftPageResponse.from(page);
  }
}

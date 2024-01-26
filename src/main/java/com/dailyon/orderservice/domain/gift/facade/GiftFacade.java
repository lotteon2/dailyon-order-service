package com.dailyon.orderservice.domain.gift.facade;

import com.dailyon.orderservice.domain.gift.api.request.GiftDto;
import com.dailyon.orderservice.domain.gift.entity.Gift;
import com.dailyon.orderservice.domain.gift.facade.response.GiftPageResponse;
import com.dailyon.orderservice.domain.gift.service.GiftService;
import com.dailyon.orderservice.domain.gift.service.request.GiftCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GiftFacade {
  private final GiftService giftService;
  private final GiftDtoMapper giftDtoMapper;

  public GiftPageResponse getReceiverGifts(Long receiverId, Pageable pageable) {
    Page<Gift> page = giftService.getGiftsByReceiver(receiverId, pageable);
    return GiftPageResponse.from(page);
  }

  public GiftPageResponse getSenderGifts(Long memberId, Pageable pageable) {
    Page<Gift> page = giftService.getGiftsBySender(memberId, pageable);
    return GiftPageResponse.from(page);
  }

  public void accept(GiftDto.createDelivery request, Long memberId) {
    GiftCommand.Accept command = giftDtoMapper.from(request);
    giftService.accept(command, memberId);
  }
}

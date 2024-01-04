package com.dailyon.orderservice.domain.order.service;

import com.dailyon.orderservice.domain.delivery.entity.Delivery;
import com.dailyon.orderservice.domain.delivery.implement.DeliveryAppender;
import com.dailyon.orderservice.domain.order.entity.Gift;
import com.dailyon.orderservice.domain.order.entity.Order;
import com.dailyon.orderservice.domain.order.implement.GiftAppender;
import com.dailyon.orderservice.domain.order.implement.GiftManager;
import com.dailyon.orderservice.domain.order.implement.GiftReader;
import com.dailyon.orderservice.domain.order.service.request.GiftCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class GiftService {
  private final GiftAppender giftAppender;
  private final GiftManager giftManager;
  private final GiftReader giftReader;
  private final DeliveryAppender deliveryAppender;

  @Transactional
  public Gift createGift(GiftCommand.RegisterGift giftCommand, String orderNo) {
    Gift gift = giftCommand.toEntity(orderNo);
    Gift savedGift = giftAppender.append(gift);
    return savedGift;
  }

  @Transactional
  public Gift update(Order order) {
    Gift gift = giftReader.read(order.getOrderNo());
    return giftManager.update(gift, order);
  }

  public Page<Gift> getGiftsByReceiver(Long receiverId, Pageable pageable) {
    return giftReader.readByReceiver(receiverId, pageable);
  }

  public Page<Gift> getGiftsBySender(Long memberId, Pageable pageable) {
    return giftReader.readBySender(memberId, pageable);
  }

  @Transactional
  public Gift accept(GiftCommand.Accept request, Long memberId) {
    Gift gift = giftReader.read(request.getOrderNo());
    Delivery delivery = request.toEntity(gift.getOrder());
    deliveryAppender.append(delivery);
    giftManager.accept(gift);
    return gift;
  }
}

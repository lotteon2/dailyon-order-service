package com.dailyon.orderservice.domain.order.implement;

import com.dailyon.orderservice.domain.order.entity.Gift;
import com.dailyon.orderservice.domain.order.entity.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GiftManager {

  public Gift update(Gift gift, Order order) {
    gift.add(order);
    gift.orderComplete();
    return gift;
  }

  public Gift accept(Gift gift) {
    gift.accept();
    return gift;
  }
}

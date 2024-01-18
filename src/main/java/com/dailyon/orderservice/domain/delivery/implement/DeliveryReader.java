package com.dailyon.orderservice.domain.delivery.implement;

import com.dailyon.orderservice.domain.delivery.entity.Delivery;
import com.dailyon.orderservice.domain.delivery.entity.enums.DeliveryStatus;
import com.dailyon.orderservice.domain.delivery.exception.DeliveryNotFoundException;
import com.dailyon.orderservice.domain.delivery.repository.DeliveryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class DeliveryReader {
  private final DeliveryRepository deliveryRepository;

  public Delivery read(String orderNo) {
    return deliveryRepository.findByOrderNo(orderNo).orElseThrow(DeliveryNotFoundException::new);
  }

  public Page<Delivery> readWithPaging(Pageable pageable, DeliveryStatus status) {
    return deliveryRepository.findWithPagingInFetch(pageable, status);
  }
}

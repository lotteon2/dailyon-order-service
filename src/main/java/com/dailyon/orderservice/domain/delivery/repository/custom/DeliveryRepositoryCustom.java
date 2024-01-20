package com.dailyon.orderservice.domain.delivery.repository.custom;

import com.dailyon.orderservice.domain.delivery.entity.Delivery;
import com.dailyon.orderservice.domain.delivery.entity.enums.DeliveryStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface DeliveryRepositoryCustom {
  Optional<Delivery> findByOrderNo(String orderNo);

  Page<Delivery> findWithPagingInFetch(Pageable pageable, DeliveryStatus status);
}

package com.dailyon.orderservice.domain.delivery.repository;

import com.dailyon.orderservice.domain.delivery.entity.Delivery;
import com.dailyon.orderservice.domain.delivery.repository.custom.DeliveryRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeliveryRepository
    extends JpaRepository<Delivery, Long>, DeliveryRepositoryCustom {}

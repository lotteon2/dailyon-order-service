package com.dailyon.orderservice.domain.refund.repository;

import com.dailyon.orderservice.domain.refund.entity.Refund;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefundRepository extends JpaRepository<Refund, Long> {}

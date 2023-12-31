package com.dailyon.orderservice.domain.refund.repository;

import com.dailyon.orderservice.domain.refund.entity.Refund;
import com.dailyon.orderservice.domain.refund.repository.custom.RefundRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefundRepository extends JpaRepository<Refund, Long>, RefundRepositoryCustom {}

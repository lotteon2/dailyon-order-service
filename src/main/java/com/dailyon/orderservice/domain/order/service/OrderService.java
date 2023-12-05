package com.dailyon.orderservice.domain.order.service;

import com.dailyon.orderservice.domain.order.implement.OrderReader;
import com.dailyon.orderservice.domain.order.implement.TOrderReader;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderService {
    private final OrderReader orderReader;
    private final TOrderReader tOrderReader;

}

package com.dailyon.orderservice.domain.order.implement;

import com.dailyon.orderservice.domain.order.repository.OrderDetailRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class OrderManager {
    private final OrderDetailRepository orderDetailRepository;

}

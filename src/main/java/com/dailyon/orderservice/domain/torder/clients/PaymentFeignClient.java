package com.dailyon.orderservice.domain.torder.clients;

import com.dailyon.orderservice.config.DefaultFeignConfig;
import com.dailyon.orderservice.domain.torder.clients.dto.PaymentDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "payment-service", configuration = DefaultFeignConfig.class)
public interface PaymentFeignClient {

    @PostMapping("/clients/payments/ready")
    String orderPaymentReady(
            @RequestHeader(value = "memberId") Long memberId,
            @RequestBody PaymentDTO param);
}

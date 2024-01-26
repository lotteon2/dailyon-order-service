package com.dailyon.orderservice.domain.order.clients;

import com.dailyon.orderservice.config.DefaultFeignConfig;
import dailyon.domain.order.clients.PaymentDTO.PaymentReadyParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(
    name = "payment-service",
    url = "${endpoint.payment-service}",
    configuration = DefaultFeignConfig.class)
public interface PaymentFeignClient {

  @PostMapping("/clients/payments/ready")
  String orderPaymentReady(
      @RequestHeader(value = "memberId") Long memberId, @RequestBody PaymentReadyParam param);
}

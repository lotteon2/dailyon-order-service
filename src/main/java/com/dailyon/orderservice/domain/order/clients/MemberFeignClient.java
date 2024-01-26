package com.dailyon.orderservice.domain.order.clients;

import com.dailyon.orderservice.config.DefaultFeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "member-service", url = "${endpoint.member-service}", configuration = DefaultFeignConfig.class)
public interface MemberFeignClient {

  @GetMapping("/clients/members/points")
  int getMyPoints(@RequestHeader(value = "memberId") Long memberId);
}

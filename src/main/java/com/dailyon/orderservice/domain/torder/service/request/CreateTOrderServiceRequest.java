package com.dailyon.orderservice.domain.torder.service.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CreateTOrderServiceRequest {
    private String orderId;
    private Long memberId;


}

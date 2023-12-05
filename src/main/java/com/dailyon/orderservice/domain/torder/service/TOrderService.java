package com.dailyon.orderservice.domain.torder.service;

import com.dailyon.orderservice.domain.torder.implement.TOrderAppender;
import com.dailyon.orderservice.domain.torder.service.request.CreateTOrderServiceRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TOrderService {
  private TOrderAppender tOrderAppender;

  public String createTOrder(CreateTOrderServiceRequest request) {
    return null;
  }
}

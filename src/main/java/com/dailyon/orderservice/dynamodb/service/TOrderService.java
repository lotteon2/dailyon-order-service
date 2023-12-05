package com.dailyon.orderservice.dynamodb.service;

import com.dailyon.orderservice.dynamodb.implement.TOrderAppender;
import com.dailyon.orderservice.dynamodb.service.request.CreateTOrderServiceRequest;
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

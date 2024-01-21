package com.dailyon.orderservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cloud.aws.messaging.core.QueueMessagingTemplate;
import org.springframework.cloud.aws.messaging.listener.SimpleMessageListenerContainer;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
@TestPropertySource(
    properties = {
      "cloud.aws.dynamodb.endpoint=http://localhost:8000",
      "cloud.aws.credentials.ACCESS_KEY_ID=testkey",
      "cloud.aws.credentials.SECRET_ACCESS_KEY=testkey",
      "endpoint.member-service:http://localhost:8080",
      "endpoint.promotion-service:http://localhost:8084",
      "endpoint.product-service=http://localhost:8085",
      "endpoint.payment-service=http://localhost:8089",
      "endpoint.auction-service=http://localhost:8083",
      "redirect_url=testRedirect_url"
    })
public class IntegrationTestSupport {

  @Autowired protected ObjectMapper objectMapper;

  @MockBean SimpleMessageListenerContainer simpleMessageListenerContainer;

  @MockBean QueueMessagingTemplate queueMessagingTemplate;
}

package com.dailyon.orderservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
@TestPropertySource(
    properties = {
      "amazon.dynamodb.endpoint=http://localhost:8000",
      "amazon.aws.accesskey=testkey",
      "amazon.aws.secretkey=testkey"
    })
public class IntegrationTestSupport {

  @Autowired protected ObjectMapper objectMapper;
}

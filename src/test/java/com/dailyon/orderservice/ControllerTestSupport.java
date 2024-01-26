package com.dailyon.orderservice;

import com.dailyon.orderservice.domain.delivery.api.DeliveryApiController;
import com.dailyon.orderservice.domain.delivery.service.DeliveryService;
import com.dailyon.orderservice.domain.order.api.OrderApiController;
import com.dailyon.orderservice.domain.order.facade.OrderFacade;
import com.dailyon.orderservice.domain.order.api.TOrderApiController;
import com.dailyon.orderservice.domain.order.facade.TOrderFacade;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(
    controllers = {
      DeliveryApiController.class,
      TOrderApiController.class,
      OrderApiController.class
    })
public class ControllerTestSupport {

  @Autowired protected MockMvc mockMvc;
  @Autowired protected ObjectMapper objectMapper;
  @MockBean protected DeliveryService deliveryService;
  @MockBean protected TOrderFacade tOrderFacade;
  @MockBean protected OrderFacade orderFacade;
}

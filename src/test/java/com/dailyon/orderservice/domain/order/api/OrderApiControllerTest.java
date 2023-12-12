package com.dailyon.orderservice.domain.order.api;

import com.dailyon.orderservice.ControllerTestSupport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class OrderApiControllerTest extends ControllerTestSupport {

  @DisplayName("주문 내역를 조회한다.")
  @Test
  void getOrders() throws Exception {
    // given
    Long memberId = 1L;
    // when // then
    mockMvc
        .perform(get("/orders").header("memberId", memberId).param("page", "0"))
        .andExpect(status().isOk());
  }
}

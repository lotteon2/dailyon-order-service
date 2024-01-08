package com.dailyon.orderservice.domain.order.api;

import com.dailyon.orderservice.ControllerTestSupport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
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

  @DisplayName("주문 내역상세 를 조회한다.")
  @Test
  void getOrderDetails() throws Exception {
    // given
    Long memberId = 1L;
    String orderNo = "testOrderNo";
    // when // then
    mockMvc
        .perform(get("/orders/{orderNo}", orderNo).header("memberId", memberId))
        .andExpect(status().isOk());
  }

  @DisplayName("주문 상세에서 배송 전인 상품을 주문 취소한다.")
  @Test
  void cancelOrderDetail() throws Exception {
    // given
    Long memberId = 1L;
    // when // then
    mockMvc
        .perform(
           delete("/orders/order-details/{orderDetailNo}", "orderDetailNo")
                .header("memberId", memberId))
        .andExpect(status().isOk());
  }
}

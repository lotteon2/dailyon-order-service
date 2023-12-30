package com.dailyon.orderservice.domain.delivery.api;

import com.dailyon.orderservice.ControllerTestSupport;
import com.dailyon.orderservice.domain.delivery.api.request.DeliveryCreateRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class DeliveryApiControllerTest extends ControllerTestSupport {

  @DisplayName("배송을 등록한다.")
  @Test
  void createDelivery() throws Exception {
    String orderId = "ORDER-001";
    String receiver = "홍길동";
    String postCode = "201-201";
    String roadAddress = "서울 특별시 강서구5길";
    String detailAddress = "강서아파트 111호";
    String phoneNumber = null;
    DeliveryCreateRequest request =
        new DeliveryCreateRequest(
            orderId, receiver, postCode, roadAddress, detailAddress, phoneNumber);

    // when // then
    mockMvc
        .perform(
            post("/deliveries")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isCreated());
  }

  @DisplayName("배송 등록 시 주문 번호는 필수이다.")
  @Test
  void createDeliveryWithNullOrderId() throws Exception {
    // given
    String orderId = null;
    String receiver = "홍길동";
    String postCode = "201-201";
    String roadAddress = "서울 특별시 강서구5길";
    String detailAddress = "강서아파트 111호";
    String phoneNumber = null;
    DeliveryCreateRequest request =
        new DeliveryCreateRequest(
            orderId, receiver, postCode, roadAddress, detailAddress, phoneNumber);

    // when // then
    mockMvc
        .perform(
            post("/deliveries")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.message").value("잘못된 요청입니다."))
        .andExpect(jsonPath("$.validation.orderId").value("주문번호는 필수 입니다."));
  }

  @DisplayName("배송 등록 시 수령인은 필수이다.")
  @Test
  void createDeliveryWithNullReceiver() throws Exception {
    // given
    String orderId = "ORDER-001";
    String receiver = null;
    String postCode = "201-201";
    String roadAddress = "서울 특별시 강서구5길";
    String detailAddress = "강서아파트 111호";
    String phoneNumber = null;
    DeliveryCreateRequest request =
        new DeliveryCreateRequest(
            orderId, receiver, postCode, roadAddress, detailAddress, phoneNumber);

    // when // then
    mockMvc
        .perform(
            post("/deliveries")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.message").value("잘못된 요청입니다."))
        .andExpect(jsonPath("$.validation.receiver").value("수령인은 필수 입니다."));
  }

  @DisplayName("배송 등록 시 우편 번호는 필수이다.")
  @Test
  void createDeliveryWithNullPostCode() throws Exception {
    // given
    String orderId = "ORDER-001";
    String receiver = "홍길동";
    String postCode = null;
    String roadAddress = "서울 특별시 강서구5길";
    String detailAddress = "강서아파트 111호";
    String phoneNumber = null;
    DeliveryCreateRequest request =
        new DeliveryCreateRequest(
            orderId, receiver, postCode, roadAddress, detailAddress, phoneNumber);

    // when // then
    mockMvc
        .perform(
            post("/deliveries")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.message").value("잘못된 요청입니다."))
        .andExpect(jsonPath("$.validation.postCode").value("우편번호는 필수 입니다."));
  }

  @DisplayName("배송 등록 시 도로명 주소는 필수이다.")
  @Test
  void createDeliveryWithNullRoadAddress() throws Exception {
    // given
    String orderId = "ORDER-001";
    String receiver = "홍길동";
    String postCode = "201-201";
    String roadAddress = null;
    String detailAddress = "강서아파트 111호";
    String phoneNumber = null;
    DeliveryCreateRequest request =
        new DeliveryCreateRequest(
            orderId, receiver, postCode, roadAddress, detailAddress, phoneNumber);

    // when // then
    mockMvc
        .perform(
            post("/deliveries")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.message").value("잘못된 요청입니다."))
        .andExpect(jsonPath("$.validation.roadAddress").value("도로명 주소는 필수 입니다."));
  }

  @DisplayName("배송 등록 시 상세 주소는 필수이다.")
  @Test
  void createDeliveryWithNullDetailAddress() throws Exception {
    // given
    String orderId = "ORDER-001";
    String receiver = "홍길동";
    String postCode = "201-201";
    String roadAddress = "서울 특별시 강서구5길";
    String detailAddress = null;
    String phoneNumber = null;
    DeliveryCreateRequest request =
        new DeliveryCreateRequest(
            orderId, receiver, postCode, roadAddress, detailAddress, phoneNumber);

    // when // then
    mockMvc
        .perform(
            post("/deliveries")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.message").value("잘못된 요청입니다."))
        .andExpect(jsonPath("$.validation.detailAddress").value("상세 주소는 필수 입니다."));
  }

  @DisplayName("주문번호로 배송 정보를 조회할 수 있다.")
  @Test
  void getDeliveryDetailByOrderId() throws Exception {
    // given
    String orderId = "ORDER-001";
    // when // then
    mockMvc.perform(get("/deliveries/orders/{orderId}", orderId)).andExpect(status().isOk());
  }
}

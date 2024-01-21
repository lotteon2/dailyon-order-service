package com.dailyon.orderservice.domain.torder.api;

import com.dailyon.orderservice.ControllerTestSupport;
import com.dailyon.orderservice.domain.order.entity.enums.OrderType;
import com.dailyon.orderservice.domain.torder.api.request.TOrderDto.TOrderCreateRequest;
import com.dailyon.orderservice.domain.torder.api.request.TOrderDto.TOrderCreateRequest.RegisterDeliveryRequest;
import com.dailyon.orderservice.domain.torder.api.request.TOrderDto.TOrderCreateRequest.RegisterItemRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import java.util.List;

import static com.dailyon.orderservice.domain.order.entity.enums.OrderType.SINGLE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class TOrderApiControllerTest extends ControllerTestSupport {

  @DisplayName("주문 정보를 입력 받아 임시 주문을 등록한다. ")
  @Test
  void createTOrderReady() throws Exception {
    // given

    RegisterDeliveryRequest deliveryRequest = new RegisterDeliveryRequest();
    deliveryRequest.setReceiver("testReceiver");
    deliveryRequest.setDetailAddress("testDetailAddress");
    deliveryRequest.setRoadAddress("testRoadAddress");
    deliveryRequest.setPostCode("testPostCode");

    RegisterItemRequest itemRequest = new RegisterItemRequest();
    itemRequest.setOrderPrice(100000);
    itemRequest.setProductId(1L);
    itemRequest.setCategoryId(1L);
    itemRequest.setCouponInfoId(1L);
    itemRequest.setQuantity(1);
    itemRequest.setSizeId(1L);
    List<RegisterItemRequest> irList = List.of(itemRequest);

    int deliveryFee = 0;
    int usedPoints = 0;
    OrderType type = SINGLE;
    int totalCouponDiscountPrice = 0;

    TOrderCreateRequest request =
        createTOrderRequest(
            deliveryFee, usedPoints, type, totalCouponDiscountPrice, deliveryRequest, irList);

    // when // then
    mockMvc
        .perform(
            post("/orders")
                .header("memberId", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isCreated());
  }

  private TOrderCreateRequest createTOrderRequest(
      int deliveryFee,
      int usedPoints,
      OrderType orderType,
      int totalCouponDiscountPrice,
      RegisterDeliveryRequest deliveryCreateRequest,
      List<RegisterItemRequest> itemRequests) {
    TOrderCreateRequest request = new TOrderCreateRequest();
    request.setDeliveryFee(deliveryFee);
    request.setPaymentType("KAKAOPAY");
    request.setUsedPoints(usedPoints);
    request.setType(orderType);
    request.setTotalCouponDiscountPrice(totalCouponDiscountPrice);

    request.setOrderItems(itemRequests);
    request.setDeliveryInfo(deliveryCreateRequest);

    return request;
  }

  @DisplayName("주문 등록 시 상품 아이디는 필수 이다. ")
  @Test
  void createTOrderReadyWithNoExistProductId() throws Exception {
    // given
    RegisterDeliveryRequest deliveryRequest = new RegisterDeliveryRequest();
    deliveryRequest.setReceiver("testReceiver");
    deliveryRequest.setDetailAddress("testDetailAddress");
    deliveryRequest.setRoadAddress("testRoadAddress");
    deliveryRequest.setPostCode("testPostCode");

    RegisterItemRequest itemRequest = new RegisterItemRequest();
    itemRequest.setOrderPrice(100000);
    itemRequest.setProductId(null);
    itemRequest.setCategoryId(1L);
    itemRequest.setCouponInfoId(1L);
    itemRequest.setQuantity(1);
    itemRequest.setSizeId(1L);
    List<RegisterItemRequest> irList = List.of(itemRequest);

    int deliveryFee = 0;
    int usedPoints = 0;
    OrderType type = SINGLE;
    int totalCouponDiscountPrice = 0;

    TOrderCreateRequest request =
            createTOrderRequest(
                    deliveryFee, usedPoints, type, totalCouponDiscountPrice, deliveryRequest, irList);

    // when // then
    mockMvc
        .perform(
            post("/orders")
                .header("memberId", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.message").value("잘못된 요청입니다."))
        .andExpect(jsonPath("$.validation.*").value("상품 아이디는 필수 입니다."));
  }

  @DisplayName("주문 등록 시 치수 아이디는 필수 이다. ")
  @Test
  void createTOrderReadyWithNoExistSizeId() throws Exception {
    // given
    RegisterDeliveryRequest deliveryRequest = new RegisterDeliveryRequest();
    deliveryRequest.setReceiver("testReceiver");
    deliveryRequest.setDetailAddress("testDetailAddress");
    deliveryRequest.setRoadAddress("testRoadAddress");
    deliveryRequest.setPostCode("testPostCode");

    RegisterItemRequest itemRequest = new RegisterItemRequest();
    itemRequest.setOrderPrice(100000);
    itemRequest.setProductId(1L);
    itemRequest.setCategoryId(1L);
    itemRequest.setCouponInfoId(1L);
    itemRequest.setQuantity(1);
    itemRequest.setSizeId(null);
    List<RegisterItemRequest> irList = List.of(itemRequest);

    int deliveryFee = 0;
    int usedPoints = 0;
    OrderType type = SINGLE;
    int totalCouponDiscountPrice = 0;

    TOrderCreateRequest request =
            createTOrderRequest(
                    deliveryFee, usedPoints, type, totalCouponDiscountPrice, deliveryRequest, irList);

    // when // then
    mockMvc
        .perform(
            post("/orders")
                .header("memberId", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.message").value("잘못된 요청입니다."))
        .andExpect(jsonPath("$.validation.*").value("치수는 필수 입니다."));
  }

  @DisplayName("주문 등록 시 주문 가격은 필수 이다. ")
  @Test
  void createTOrderReadyWithNoExistOrderPrice() throws Exception {
    // given
    RegisterDeliveryRequest deliveryRequest = new RegisterDeliveryRequest();
    deliveryRequest.setReceiver("testReceiver");
    deliveryRequest.setDetailAddress("testDetailAddress");
    deliveryRequest.setRoadAddress("testRoadAddress");
    deliveryRequest.setPostCode("testPostCode");

    RegisterItemRequest itemRequest = new RegisterItemRequest();
    itemRequest.setOrderPrice(null);
    itemRequest.setProductId(1L);
    itemRequest.setCategoryId(1L);
    itemRequest.setCouponInfoId(1L);
    itemRequest.setQuantity(1);
    itemRequest.setSizeId(1L);
    List<RegisterItemRequest> irList = List.of(itemRequest);

    int deliveryFee = 0;
    int usedPoints = 0;
    OrderType type = SINGLE;
    int totalCouponDiscountPrice = 0;

    TOrderCreateRequest request =
            createTOrderRequest(
                    deliveryFee, usedPoints, type, totalCouponDiscountPrice, deliveryRequest, irList);

    // when // then
    mockMvc
        .perform(
            post("/orders")
                .header("memberId", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.message").value("잘못된 요청입니다."))
        .andExpect(jsonPath("$.validation.*").value("주문 가격은 필수 입니다."));
  }

  @DisplayName("주문 등록 시 상품 수량은 0개 일 수 없다. ")
  @Test
  void createTOrderReadyWithZeroQuantity() throws Exception {
    // given
    RegisterDeliveryRequest deliveryRequest = new RegisterDeliveryRequest();
    deliveryRequest.setReceiver("testReceiver");
    deliveryRequest.setDetailAddress("testDetailAddress");
    deliveryRequest.setRoadAddress("testRoadAddress");
    deliveryRequest.setPostCode("testPostCode");

    RegisterItemRequest itemRequest = new RegisterItemRequest();
    itemRequest.setOrderPrice(100000);
    itemRequest.setProductId(1L);
    itemRequest.setCategoryId(1L);
    itemRequest.setCouponInfoId(1L);
    itemRequest.setQuantity(0);
    itemRequest.setSizeId(1L);
    List<RegisterItemRequest> irList = List.of(itemRequest);

    int deliveryFee = 0;
    int usedPoints = 0;
    OrderType type = SINGLE;
    int totalCouponDiscountPrice = 0;

    TOrderCreateRequest request =
            createTOrderRequest(
                    deliveryFee, usedPoints, type, totalCouponDiscountPrice, deliveryRequest, irList);

    // when // then
    mockMvc
        .perform(
            post("/orders")
                .header("memberId", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.message").value("잘못된 요청입니다."))
        .andExpect(jsonPath("$.validation.*").value("상품 수량은 0개일 수 없습니다."));
  }

  @DisplayName("주문 등록 시 포인트트 사용한 경우 사용한 포인트는 0 이상이어야 한다.")
  @Test
  void createTOrderReadyWithMinusPoints() throws Exception {
    // given
    RegisterDeliveryRequest deliveryRequest = new RegisterDeliveryRequest();
    deliveryRequest.setReceiver("testReceiver");
    deliveryRequest.setDetailAddress("testDetailAddress");
    deliveryRequest.setRoadAddress("testRoadAddress");
    deliveryRequest.setPostCode("testPostCode");

    RegisterItemRequest itemRequest = new RegisterItemRequest();
    itemRequest.setOrderPrice(100000);
    itemRequest.setProductId(1L);
    itemRequest.setCategoryId(1L);
    itemRequest.setCouponInfoId(1L);
    itemRequest.setQuantity(1);
    itemRequest.setSizeId(1L);
    List<RegisterItemRequest> irList = List.of(itemRequest);

    int deliveryFee = 0;
    int usedPoints = -1;
    OrderType type = SINGLE;
    int totalCouponDiscountPrice = 0;

    TOrderCreateRequest request =
            createTOrderRequest(
                    deliveryFee, usedPoints, type, totalCouponDiscountPrice, deliveryRequest, irList);

    // when // then
    mockMvc
        .perform(
            post("/orders")
                .header("memberId", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.message").value("잘못된 요청입니다."))
        .andExpect(jsonPath("$.validation.*").value("포인트는 0이상 이어야 합니다."));
  }

  @DisplayName("주문 등록 시 주문 타입은 필수이다.")
  @Test
  void createTOrderReadyWithNoExistOrderType() throws Exception {
    // given
    RegisterDeliveryRequest deliveryRequest = new RegisterDeliveryRequest();
    deliveryRequest.setReceiver("testReceiver");
    deliveryRequest.setDetailAddress("testDetailAddress");
    deliveryRequest.setRoadAddress("testRoadAddress");
    deliveryRequest.setPostCode("testPostCode");

    RegisterItemRequest itemRequest = new RegisterItemRequest();
    itemRequest.setOrderPrice(100000);
    itemRequest.setProductId(1L);
    itemRequest.setCategoryId(1L);
    itemRequest.setCouponInfoId(1L);
    itemRequest.setQuantity(1);
    itemRequest.setSizeId(1L);
    List<RegisterItemRequest> irList = List.of(itemRequest);

    int deliveryFee = 0;
    int usedPoints = 0;
    OrderType type = null;
    int totalCouponDiscountPrice = 0;

    TOrderCreateRequest request =
            createTOrderRequest(
                    deliveryFee, usedPoints, type, totalCouponDiscountPrice, deliveryRequest, irList);

    // when // then
    mockMvc
        .perform(
            post("/orders")
                .header("memberId", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.message").value("잘못된 요청입니다."))
        .andExpect(jsonPath("$.validation.*").value("주문 타입은 필수 입니다."));
  }

  @DisplayName("주문 등록 배송비는 0이상이어야 한다.")
  @Test
  void createTOrderReadyWithMinusDeliveryFee() throws Exception {
    // given
    RegisterDeliveryRequest deliveryRequest = new RegisterDeliveryRequest();
    deliveryRequest.setReceiver("testReceiver");
    deliveryRequest.setDetailAddress("testDetailAddress");
    deliveryRequest.setRoadAddress("testRoadAddress");
    deliveryRequest.setPostCode("testPostCode");

    RegisterItemRequest itemRequest = new RegisterItemRequest();
    itemRequest.setOrderPrice(100000);
    itemRequest.setProductId(1L);
    itemRequest.setCategoryId(1L);
    itemRequest.setCouponInfoId(1L);
    itemRequest.setQuantity(1);
    itemRequest.setSizeId(1L);
    List<RegisterItemRequest> irList = List.of(itemRequest);

    int deliveryFee = -1;
    int usedPoints = 0;
    OrderType type = SINGLE;
    int totalCouponDiscountPrice = 0;

    TOrderCreateRequest request =
            createTOrderRequest(
                    deliveryFee, usedPoints, type, totalCouponDiscountPrice, deliveryRequest, irList);

    // when // then
    mockMvc
        .perform(
            post("/orders")
                .header("memberId", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.message").value("잘못된 요청입니다."))
        .andExpect(jsonPath("$.validation.*").value("배송비는 0이상 이어야 합니다."));
  }
}

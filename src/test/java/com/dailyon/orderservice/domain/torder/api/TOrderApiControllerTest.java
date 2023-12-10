package com.dailyon.orderservice.domain.torder.api;

import com.dailyon.orderservice.ControllerTestSupport;
import com.dailyon.orderservice.domain.torder.api.request.TOrderRequest.TOrderCreateRequest;
import com.dailyon.orderservice.domain.torder.api.request.TOrderRequest.TOrderCreateRequest.DeliveryInfo;
import com.dailyon.orderservice.domain.torder.api.request.TOrderRequest.TOrderCreateRequest.OrderInfo;
import com.dailyon.orderservice.domain.torder.api.request.TOrderRequest.TOrderCreateRequest.OrderItem;
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
    List<OrderItem> orderItems =
        List.of(
            OrderItem.builder()
                .orderPrice(100000)
                .referralCode("test")
                .categoryId(1L)
                .productId(1L)
                .couponInfoId(1L)
                .quantity(1)
                .sizeId(1L)
                .build());
    OrderInfo orderInfo =
        OrderInfo.builder()
            .type(SINGLE)
            .deliveryFee(0)
            .totalCouponDiscountPrice(0)
            .usedPoints(0)
            .build();
    DeliveryInfo deliveryInfo =
        DeliveryInfo.builder()
            .receiver("수령인")
            .postCode("201-201")
            .roadAddress("서울시 강남구")
            .detailAddress("비트교육센터")
            .build();

    String paymentType = "KAKAOPAY";
    TOrderCreateRequest request =
        new TOrderCreateRequest(orderItems, orderInfo, deliveryInfo, paymentType);

    // when // then
    mockMvc
        .perform(
            post("/orders")
                .header("memberId", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isCreated());
  }

  @DisplayName("주문 등록 시 상품 아이디는 필수 이다. ")
  @Test
  void createTOrderReadyWithNoExistProductId() throws Exception {
    // given
    List<OrderItem> orderItems =
        List.of(
            OrderItem.builder()
                .orderPrice(100000)
                .referralCode("test")
                .categoryId(1L)
                .couponInfoId(1L)
                .quantity(1)
                .sizeId(1L)
                .build());
    OrderInfo orderInfo =
        OrderInfo.builder()
            .type(SINGLE)
            .deliveryFee(0)
            .totalCouponDiscountPrice(0)
            .usedPoints(0)
            .build();
    DeliveryInfo deliveryInfo =
        DeliveryInfo.builder()
            .receiver("수령인")
            .postCode("201-201")
            .roadAddress("서울시 강남구")
            .detailAddress("비트교육센터")
            .build();

    String paymentType = "KAKAOPAY";
    TOrderCreateRequest request =
        new TOrderCreateRequest(orderItems, orderInfo, deliveryInfo, paymentType);

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

  @DisplayName("주문 등록 시 상품 아이디는 필수 이다. ")
  @Test
  void createTOrderReadyWithNoExistCategoryId() throws Exception {
    // given
    List<OrderItem> orderItems =
        List.of(
            OrderItem.builder()
                .orderPrice(100000)
                .referralCode("test")
                .productId(1L)
                .couponInfoId(1L)
                .quantity(1)
                .sizeId(1L)
                .build());
    OrderInfo orderInfo =
        OrderInfo.builder()
            .type(SINGLE)
            .deliveryFee(0)
            .totalCouponDiscountPrice(0)
            .usedPoints(0)
            .build();
    DeliveryInfo deliveryInfo =
        DeliveryInfo.builder()
            .receiver("수령인")
            .postCode("201-201")
            .roadAddress("서울시 강남구")
            .detailAddress("비트교육센터")
            .build();

    String paymentType = "KAKAOPAY";
    TOrderCreateRequest request =
        new TOrderCreateRequest(orderItems, orderInfo, deliveryInfo, paymentType);

    // when // then
    mockMvc
        .perform(
            post("/orders")
                .header("memberId", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.message").value("잘못된 요청입니다."))
        .andExpect(jsonPath("$.validation.*").value("카테고리 아이디는 필수 입니다."));
  }

  @DisplayName("주문 등록 시 치수 아이디는 필수 이다. ")
  @Test
  void createTOrderReadyWithNoExistSizeId() throws Exception {
    // given
    List<OrderItem> orderItems =
        List.of(
            OrderItem.builder()
                .orderPrice(100000)
                .referralCode("test")
                .productId(1L)
                .categoryId(1L)
                .couponInfoId(1L)
                .quantity(1)
                .build());
    OrderInfo orderInfo =
        OrderInfo.builder()
            .type(SINGLE)
            .deliveryFee(0)
            .totalCouponDiscountPrice(0)
            .usedPoints(0)
            .build();
    DeliveryInfo deliveryInfo =
        DeliveryInfo.builder()
            .receiver("수령인")
            .postCode("201-201")
            .roadAddress("서울시 강남구")
            .detailAddress("비트교육센터")
            .build();

    String paymentType = "KAKAOPAY";
    TOrderCreateRequest request =
        new TOrderCreateRequest(orderItems, orderInfo, deliveryInfo, paymentType);

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
    List<OrderItem> orderItems =
        List.of(
            OrderItem.builder()
                .referralCode("test")
                .productId(1L)
                .categoryId(1L)
                .couponInfoId(1L)
                .quantity(1)
                .sizeId(1L)
                .build());
    OrderInfo orderInfo =
        OrderInfo.builder()
            .type(SINGLE)
            .deliveryFee(0)
            .totalCouponDiscountPrice(0)
            .usedPoints(0)
            .build();
    DeliveryInfo deliveryInfo =
        DeliveryInfo.builder()
            .receiver("수령인")
            .postCode("201-201")
            .roadAddress("서울시 강남구")
            .detailAddress("비트교육센터")
            .build();

    String paymentType = "KAKAOPAY";
    TOrderCreateRequest request =
        new TOrderCreateRequest(orderItems, orderInfo, deliveryInfo, paymentType);

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
    List<OrderItem> orderItems =
        List.of(
            OrderItem.builder()
                .orderPrice(100000)
                .referralCode("test")
                .productId(1L)
                .categoryId(1L)
                .couponInfoId(1L)
                .quantity(0)
                .sizeId(1L)
                .build());
    OrderInfo orderInfo =
        OrderInfo.builder()
            .type(SINGLE)
            .deliveryFee(0)
            .totalCouponDiscountPrice(0)
            .usedPoints(0)
            .build();
    DeliveryInfo deliveryInfo =
        DeliveryInfo.builder()
            .receiver("수령인")
            .postCode("201-201")
            .roadAddress("서울시 강남구")
            .detailAddress("비트교육센터")
            .build();

    String paymentType = "KAKAOPAY";
    TOrderCreateRequest request =
        new TOrderCreateRequest(orderItems, orderInfo, deliveryInfo, paymentType);

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
    List<OrderItem> orderItems =
        List.of(
            OrderItem.builder()
                .orderPrice(100000)
                .referralCode("test")
                .productId(1L)
                .categoryId(1L)
                .couponInfoId(1L)
                .quantity(1)
                .sizeId(1L)
                .build());
    OrderInfo orderInfo =
        OrderInfo.builder()
            .type(SINGLE)
            .deliveryFee(0)
            .totalCouponDiscountPrice(0)
            .usedPoints(-1)
            .build();
    DeliveryInfo deliveryInfo =
        DeliveryInfo.builder()
            .receiver("수령인")
            .postCode("201-201")
            .roadAddress("서울시 강남구")
            .detailAddress("비트교육센터")
            .build();

    String paymentType = "KAKAOPAY";
    TOrderCreateRequest request =
        new TOrderCreateRequest(orderItems, orderInfo, deliveryInfo, paymentType);

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
    List<OrderItem> orderItems =
        List.of(
            OrderItem.builder()
                .orderPrice(100000)
                .referralCode("test")
                .productId(1L)
                .categoryId(1L)
                .couponInfoId(1L)
                .quantity(1)
                .sizeId(1L)
                .build());
    OrderInfo orderInfo =
        OrderInfo.builder().deliveryFee(0).totalCouponDiscountPrice(0).usedPoints(-1).build();
    DeliveryInfo deliveryInfo =
        DeliveryInfo.builder()
            .receiver("수령인")
            .postCode("201-201")
            .roadAddress("서울시 강남구")
            .detailAddress("비트교육센터")
            .build();

    String paymentType = "KAKAOPAY";
    TOrderCreateRequest request =
        new TOrderCreateRequest(orderItems, orderInfo, deliveryInfo, paymentType);

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
    List<OrderItem> orderItems =
        List.of(
            OrderItem.builder()
                .orderPrice(100000)
                .referralCode("test")
                .productId(1L)
                .categoryId(1L)
                .couponInfoId(1L)
                .quantity(1)
                .sizeId(1L)
                .build());
    OrderInfo orderInfo =
        OrderInfo.builder()
            .type(SINGLE)
            .deliveryFee(-1)
            .totalCouponDiscountPrice(0)
            .usedPoints(0)
            .build();
    DeliveryInfo deliveryInfo =
        DeliveryInfo.builder()
            .receiver("수령인")
            .postCode("201-201")
            .roadAddress("서울시 강남구")
            .detailAddress("비트교육센터")
            .build();

    String paymentType = "KAKAOPAY";
    TOrderCreateRequest request =
        new TOrderCreateRequest(orderItems, orderInfo, deliveryInfo, paymentType);

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

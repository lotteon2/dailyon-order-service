package com.dailyon.orderservice.domain.torder.service;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
import com.amazonaws.services.dynamodbv2.model.Projection;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
import com.amazonaws.services.dynamodbv2.util.TableUtils;
import com.dailyon.orderservice.ContainerBaseTestSupport;
import com.dailyon.orderservice.common.exception.InvalidParamException;
import com.dailyon.orderservice.common.utils.OrderNoGenerator;
import com.dailyon.orderservice.domain.order.entity.enums.OrderType;
import com.dailyon.orderservice.domain.order.exception.OrderNotFoundException;
import com.dailyon.orderservice.domain.torder.entity.TOrder;
import com.dailyon.orderservice.domain.torder.entity.TOrderDetail;
import com.dailyon.orderservice.domain.torder.exception.InsufficientStockException;
import com.dailyon.orderservice.domain.torder.repository.OrderDynamoRepository;
import com.dailyon.orderservice.domain.torder.service.request.TOrderCommand;
import com.dailyon.orderservice.domain.torder.service.request.TOrderCommand.RegisterOrderItem;
import dailyon.domain.order.clients.CouponDTO.ProductCouponDTO;
import dailyon.domain.order.clients.ProductDTO.OrderProductListDTO.OrderProductDTO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

import static com.dailyon.orderservice.domain.order.entity.enums.OrderType.SINGLE;
import static org.assertj.core.api.Assertions.*;

class TOrderServiceTest extends ContainerBaseTestSupport {

  @Autowired AmazonDynamoDB dynamoDB;
  @Autowired DynamoDBMapper dynamoDBMapper;
  @Autowired OrderDynamoRepository orderDynamoRepository;
  @Autowired TOrderService tOrderService;

  @BeforeEach
  void setup() {
    CreateTableRequest createTableRequest =
        dynamoDBMapper
            .generateCreateTableRequest(TOrder.class)
            .withProvisionedThroughput(new ProvisionedThroughput(1L, 1L));

    createTableRequest
        .getGlobalSecondaryIndexes()
        .forEach(
            idx ->
                idx.withProvisionedThroughput(new ProvisionedThroughput(1L, 1L))
                    .withProjection(new Projection().withProjectionType("ALL")));

    TableUtils.createTableIfNotExists(dynamoDB, createTableRequest);
  }

  @AfterEach
  void after() {
    TableUtils.deleteTableIfExists(
        dynamoDB, dynamoDBMapper.generateDeleteTableRequest(TOrder.class));
  }

  @DisplayName("입력 정보를 바탕으로 임시 주문 정보를 생성한다.")
  @Test
  void createTOrder() {
    // given
    OrderProductDTO orderProductDTO =
        OrderProductDTO.builder()
            .productId(1L)
            .price(15000)
            .productName("나이키 슬리퍼")
            .categoryId(1L)
            .gender("MAN")
            .imgUrl("testUrl")
            .sizeId(1L)
            .sizeName("265")
            .stock(30)
            .build();
    List<OrderProductDTO> orderProductDTOList = List.of(orderProductDTO);
    RegisterOrderItem orderItem =
        RegisterOrderItem.builder()
            .productId(1L)
            .sizeId(1L)
            .quantity(2)
            .referralCode("reCode")
            .build();

    Map<Long, RegisterOrderItem> productInfoMap = Map.of(1L, orderItem);

    ProductCouponDTO couponDTO =
        ProductCouponDTO.builder()
            .maxDiscountAmount(0L)
            .minPurchaseAmount(0L)
            .productId(1L)
            .couponName("2000원 할인쿠폰")
            .couponInfoId(1L)
            .discountType("FIXED_AMOUNT")
            .discountValue(2000L)
            .build();

    Map<Long, ProductCouponDTO> couponInfoMap = Map.of(1L, couponDTO);

    TOrderCommand.RegisterTOrder request =
        TOrderCommand.RegisterTOrder.builder()
            .orderProductDTOList(orderProductDTOList)
            .productInfoMap(productInfoMap)
            .couponInfoMap(couponInfoMap)
            .deliveryFee(0)
            .totalCouponDiscountPrice(0)
            .usedPoints(0)
            .paymentType("KAKAOPAY")
            .type(SINGLE)
            .build();

    Long memberId = 1L;

    // when
    TOrder tOrder = tOrderService.createTOrder(request, memberId);
    // then
    TOrder getTOrder = orderDynamoRepository.findById(tOrder.getId()).get();
    assertThat(getTOrder).isNotNull();
    assertThat(getTOrder.getMemberId()).isEqualTo(memberId);
    assertThat(getTOrder.getOrderDetails())
        .isNotEmpty()
        .hasSize(1)
        .extracting(
            "orderNo",
            "productId",
            "productSizeId",
            "couponInfoId",
            "productName",
            "productSize",
            "productGender",
            "productImgUrl",
            "orderPrice",
            "couponName",
            "couponDiscountPrice")
        .containsExactlyInAnyOrder(
            tuple(
                tOrder.getId(),
                1L,
                1L,
                1L,
                "나이키 슬리퍼",
                "265",
                "MAN",
                "testUrl",
                28000,
                "2000원 할인쿠폰",
                2000));
  }

  @DisplayName("주문 시 최소 주문 금액보다 적은 금액으로 주문요청 시 예외가 발생한다.")
  @Test
  void createTOrderWithInvalidCouponParam_minPurchaseAmount() {
    // given
    OrderProductDTO orderProductDTO =
        OrderProductDTO.builder()
            .productId(1L)
            .price(15000)
            .productName("나이키 슬리퍼")
            .categoryId(1L)
            .gender("MAN")
            .imgUrl("testUrl")
            .sizeId(1L)
            .sizeName("265")
            .stock(30)
            .build();
    List<OrderProductDTO> orderProductDTOList = List.of(orderProductDTO);
    RegisterOrderItem orderItem =
        RegisterOrderItem.builder()
            .productId(1L)
            .sizeId(1L)
            .quantity(2)
            .referralCode("reCode")
            .build();

    Map<Long, RegisterOrderItem> productInfoMap = Map.of(1L, orderItem);
    ProductCouponDTO couponDTO =
        ProductCouponDTO.builder()
            .minPurchaseAmount(30001L)
            .maxDiscountAmount(5000L)
            .productId(1L)
            .couponName("2000원 할인쿠폰")
            .couponInfoId(1L)
            .discountType("FIXED_AMOUNT")
            .discountValue(2000L)
            .build();
    Map<Long, ProductCouponDTO> couponInfoMap = Map.of(1L, couponDTO);

    TOrderCommand.RegisterTOrder request =
        TOrderCommand.RegisterTOrder.builder()
            .orderProductDTOList(orderProductDTOList)
            .productInfoMap(productInfoMap)
            .couponInfoMap(couponInfoMap)
            .deliveryFee(0)
            .totalCouponDiscountPrice(0)
            .usedPoints(0)
            .paymentType("KAKAOPAY")
            .type(SINGLE)
            .build();

    Long memberId = 1L;
    // when then
    assertThatThrownBy(() -> tOrderService.createTOrder(request, memberId))
        .isInstanceOf(InvalidParamException.class)
        .hasMessage("요청한 값이 올바르지 않습니다.");
  }

  @DisplayName("정률 할인이 적용된 주문의 경우, 주문 할인 금액이 쿠폰의 최대 할인 금액을 넘을 수 없다.")
  @Test
  void createTOrderWithInvalidCouponParam_maxDiscountAmount() {
    // given
    OrderProductDTO orderProductDTO =
        OrderProductDTO.builder()
            .productId(1L)
            .price(50000000)
            .productName("샤넬 백")
            .categoryId(1L)
            .gender("WOMAN")
            .imgUrl("testUrl")
            .sizeId(1L)
            .sizeName("free")
            .stock(30)
            .build();
    List<OrderProductDTO> orderProductDTOList = List.of(orderProductDTO);
    RegisterOrderItem orderItem =
        RegisterOrderItem.builder()
            .productId(1L)
            .sizeId(1L)
            .quantity(1)
            .referralCode("reCode")
            .build();

    Map<Long, RegisterOrderItem> productInfoMap = Map.of(1L, orderItem);
    ProductCouponDTO couponDTO =
        ProductCouponDTO.builder()
            .minPurchaseAmount(0L)
            .maxDiscountAmount(100000L)
            .productId(1L)
            .couponName("5% 할인쿠폰")
            .couponInfoId(1L)
            .discountType("PERCENTAGE")
            .discountValue(5L)
            .build();
    Map<Long, ProductCouponDTO> couponInfoMap = Map.of(1L, couponDTO);

    TOrderCommand.RegisterTOrder request =
        TOrderCommand.RegisterTOrder.builder()
            .orderProductDTOList(orderProductDTOList)
            .productInfoMap(productInfoMap)
            .couponInfoMap(couponInfoMap)
            .deliveryFee(0)
            .totalCouponDiscountPrice(0)
            .usedPoints(0)
            .paymentType("KAKAOPAY")
            .type(SINGLE)
            .build();

    Long memberId = 1L;
    // when then
    String orderId = tOrderService.createTOrder(request, memberId).getId();
    TOrder tOrder = orderDynamoRepository.findById(orderId).get();
    Long totalAmount = tOrder.calculateTotalAmount();
    assertThat(totalAmount).isEqualTo(49900000);
  }

  @DisplayName("재고보다 많은 수량을 주문할 수 없다.")
  @Test
  void createTOrderWithInSufficientStock() {
    // given
    OrderProductDTO orderProductDTO =
        OrderProductDTO.builder()
            .productId(1L)
            .price(15000)
            .productName("나이키 슬리퍼")
            .categoryId(1L)
            .gender("MAN")
            .imgUrl("testUrl")
            .sizeId(1L)
            .sizeName("265")
            .stock(1)
            .build();
    List<OrderProductDTO> orderProductDTOList = List.of(orderProductDTO);
    RegisterOrderItem orderItem =
        RegisterOrderItem.builder()
            .productId(1L)
            .sizeId(1L)
            .quantity(2)
            .referralCode("reCode")
            .build();

    Map<Long, RegisterOrderItem> productInfoMap = Map.of(1L, orderItem);
    ProductCouponDTO couponDTO =
        ProductCouponDTO.builder()
            .maxDiscountAmount(0L)
            .minPurchaseAmount(0L)
            .productId(1L)
            .couponName("2000원 할인쿠폰")
            .couponInfoId(1L)
            .discountType("FIXED_AMOUNT")
            .discountValue(2000L)
            .build();
    Map<Long, ProductCouponDTO> couponInfoMap = Map.of(1L, couponDTO);

    TOrderCommand.RegisterTOrder request =
        TOrderCommand.RegisterTOrder.builder()
            .orderProductDTOList(orderProductDTOList)
            .productInfoMap(productInfoMap)
            .couponInfoMap(couponInfoMap)
            .deliveryFee(0)
            .totalCouponDiscountPrice(0)
            .usedPoints(0)
            .paymentType("KAKAOPAY")
            .type(SINGLE)
            .build();

    Long memberId = 1L;
    // when
    assertThatThrownBy(() -> tOrderService.createTOrder(request, memberId))
        .isInstanceOf(InsufficientStockException.class)
        .hasMessage("재고가 부족합니다.");
  }

  @DisplayName("주문 번호로 임시 주문 정보를 조회 한다.")
  @Test
  void getTOrder() {
    // given
    Long memberId = 1L;
    String orderId = OrderNoGenerator.generate(1L);
    TOrder tOrder = createOrder(orderId, memberId, SINGLE);

    TOrderDetail tOrderDetail1 =
        createTOrderDetail(
            orderId, 1L, 2L, 3L, "나이키신발", 1, "260", "MAN", "testUrl", 54000, "10%할인쿠폰", 6000);

    TOrderDetail tOrderDetail2 =
        createTOrderDetail(
            orderId, 2L, 3L, null, "나이키 양말", 1, "260", "MAN", "testUrl", 30000, null, 0);

    tOrder.setOrderDetails(List.of(tOrderDetail1, tOrderDetail2));
    orderDynamoRepository.save(tOrder);
    // when
    TOrder getTOrder = tOrderService.getTOrder(orderId);
    // then
    assertThat(getTOrder).isNotNull();
    assertThat(getTOrder.getId()).isEqualTo(orderId);
  }

  @DisplayName("존재하지 않는 주문번호로 조회 시 예외가 발생한다.")
  @Test
  void getTOrderWithNoExistOrderID() {
    // given
    String noExistOrderId = "noExistORderId";
    // when // then
    assertThatThrownBy(() -> tOrderService.getTOrder(noExistOrderId))
        .isInstanceOf(OrderNotFoundException.class)
        .hasMessage("해당 주문번호에 해당하는 주문이 존재하지 않습니다.");
  }

  private TOrder createOrder(String orderId, Long memberId, OrderType type) {
    return TOrder.builder().id(orderId).memberId(memberId).type(type.name()).build();
  }

  private TOrderDetail createTOrderDetail(
      String orderNo,
      Long productId,
      Long productSizeId,
      Long couponInfoId,
      String productName,
      Integer productQuantity,
      String productSize,
      String productGender,
      String productImgUrl,
      Integer orderPrice,
      String couponName,
      Integer couponDiscountPrice) {
    return TOrderDetail.builder()
        .orderNo(orderNo)
        .productId(productId)
        .productSizeId(productSizeId)
        .couponInfoId(couponInfoId)
        .productName(productName)
        .productQuantity(productQuantity)
        .productSize(productSize)
        .productGender(productGender)
        .productImgUrl(productImgUrl)
        .orderPrice(orderPrice)
        .couponName(couponName)
        .couponDiscountPrice(couponDiscountPrice)
        .build();
  }
}

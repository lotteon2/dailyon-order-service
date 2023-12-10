package com.dailyon.orderservice.domain.torder.service;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
import com.amazonaws.services.dynamodbv2.util.TableUtils;
import com.dailyon.orderservice.ContainerBaseTestSupport;
import com.dailyon.orderservice.domain.order.entity.enums.OrderType;
import com.dailyon.orderservice.domain.torder.clients.dto.CouponDTO.ProductCouponDTO;
import com.dailyon.orderservice.domain.torder.clients.dto.ProductDTO.OrderProductListDTO.OrderProductDTO;
import com.dailyon.orderservice.domain.torder.entity.TOrder;
import com.dailyon.orderservice.domain.torder.facade.request.TOrderFacadeRequest.TOrderFacadeCreateRequest.OrderProductInfo;
import com.dailyon.orderservice.domain.torder.repository.OrderDynamoRepository;
import com.dailyon.orderservice.domain.torder.service.request.TOrderServiceRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

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
    OrderProductInfo opInfo =
        OrderProductInfo.builder()
            .quantity(2)
            .productId(1L)
            .referralCode("reCode")
            .sizeId(1L)
            .build();

    Map<Long, OrderProductInfo> productInfoMap = Map.of(1L, opInfo);
    ProductCouponDTO couponDTO =
        ProductCouponDTO.builder()
            .productId(1L)
            .couponName("2000원 할인쿠폰")
            .couponInfoId(1L)
            .discountType("FIXED_AMOUNT")
            .discountValue(2000L)
            .build();
    Map<Long, ProductCouponDTO> couponInfoMap = Map.of(1L, couponDTO);

    TOrderServiceRequest request =
        TOrderServiceRequest.builder()
            .orderProductDTOList(orderProductDTOList)
            .productInfoMap(productInfoMap)
            .couponInfoMap(couponInfoMap)
            .type(OrderType.SINGLE)
            .build();

    Long memberId = 1L;
    // when
    String orderId = tOrderService.createTOrder(request, 1L);
    // then
    TOrder getTOrder = orderDynamoRepository.findById(orderId).get();
    assertThat(getTOrder).isNotNull();
    assertThat(getTOrder.getMemberId()).isEqualTo(memberId);
    assertThat(getTOrder.getOrderDetails())
        .isNotEmpty()
        .hasSize(1)
        .extracting(
            "orderId",
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
                orderId,
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
}

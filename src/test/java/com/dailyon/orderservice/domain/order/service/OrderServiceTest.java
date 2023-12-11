package com.dailyon.orderservice.domain.order.service;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
import com.amazonaws.services.dynamodbv2.util.TableUtils;
import com.dailyon.orderservice.ContainerBaseTestSupport;
import com.dailyon.orderservice.common.utils.OrderNoGenerator;
import com.dailyon.orderservice.domain.order.entity.Order;
import com.dailyon.orderservice.domain.order.entity.enums.OrderType;
import com.dailyon.orderservice.domain.torder.entity.TOrder;
import com.dailyon.orderservice.domain.torder.entity.TOrderDetail;
import com.dailyon.orderservice.domain.torder.repository.OrderDynamoRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import java.util.List;

import static com.dailyon.orderservice.domain.order.entity.enums.OrderType.SINGLE;
import static org.assertj.core.api.Assertions.assertThat;

class OrderServiceTest extends ContainerBaseTestSupport {

  @Autowired AmazonDynamoDB dynamoDB;
  @Autowired DynamoDBMapper dynamoDBMapper;
  @Autowired OrderDynamoRepository orderDynamoRepository;
  @Autowired OrderService orderService;
  @Autowired EntityManager em;

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

  @DisplayName("임시 주문 정보를 통해 주문을 생성한다.")
  @Test
  void createOrder() {
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
    // when
    orderDynamoRepository.save(tOrder);
    // when
    Order order = orderService.createOrder(tOrder);
    // then
    assertThat(order.getOrderId()).isNotNull().isEqualTo(tOrder.getId());
  }

  private TOrder createOrder(String orderId, Long memberId, OrderType type) {
    return TOrder.builder().id(orderId).memberId(memberId).type(type.name()).build();
  }

  private TOrderDetail createTOrderDetail(
      String orderId,
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
        .orderId(orderId)
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

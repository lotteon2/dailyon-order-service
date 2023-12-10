package com.dailyon.orderservice.domain.torder.repository;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
import com.amazonaws.services.dynamodbv2.util.TableUtils;
import com.dailyon.orderservice.ContainerBaseTestSupport;
import com.dailyon.orderservice.common.utils.OrderNoGenerator;
import com.dailyon.orderservice.domain.order.entity.enums.OrderType;
import com.dailyon.orderservice.domain.torder.entity.TOrder;
import com.dailyon.orderservice.domain.torder.entity.TOrderDetail;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static com.dailyon.orderservice.domain.order.entity.enums.OrderType.SINGLE;
import static org.assertj.core.api.Assertions.assertThat;

class OrderDynamoRepositoryTest extends ContainerBaseTestSupport {

  @Autowired AmazonDynamoDB dynamoDB;
  @Autowired DynamoDBMapper dynamoDBMapper;
  @Autowired OrderDynamoRepository orderDynamoRepository;

  @BeforeEach
  void setup() {
    CreateTableRequest createTableRequest =
        dynamoDBMapper
            .generateCreateTableRequest(TOrder.class)
            .withProvisionedThroughput(new ProvisionedThroughput(1L, 1L));

    TableUtils.createTableIfNotExists(dynamoDB, createTableRequest);
  }

  // DynamoDB에선 테이블 row를 날리는게 더 비용이 커서 테이블을 그냥 다시 만든다
  @AfterEach
  void after() {
    TableUtils.deleteTableIfExists(
        dynamoDB, dynamoDBMapper.generateDeleteTableRequest(TOrder.class));
  }

  @Test
  @DisplayName("Order가 정상적으로 DynamoDB에 저장된다")
  void saveOrder() {
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

    // then
    TOrder savedTOrder = orderDynamoRepository.findAll().iterator().next();
    assertThat(savedTOrder.getId()).isEqualTo(orderId);
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

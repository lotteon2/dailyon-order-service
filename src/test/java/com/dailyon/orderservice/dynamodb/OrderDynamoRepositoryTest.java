package com.dailyon.orderservice.dynamodb;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
import com.amazonaws.services.dynamodbv2.util.TableUtils;
import com.dailyon.orderservice.domain.order.entity.enums.OrderType;
import com.dailyon.orderservice.domain.order.utils.OrderNoGenerator;
import com.dailyon.orderservice.dynamodb.entity.Order;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import static com.dailyon.orderservice.domain.order.entity.enums.OrderType.SINGLE;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@TestPropertySource(properties = "embedded-dynamodb.use=true")
class OrderDynamoRepositoryTest {

  @Autowired AmazonDynamoDB dynamoDB;

  @Autowired DynamoDBMapper dynamoDBMapper;

  @Autowired OrderDynamoRepository orderDynamoRepository;

  @BeforeEach
  void setup() {
    CreateTableRequest createTableRequest =
        dynamoDBMapper
            .generateCreateTableRequest(Order.class)
            .withProvisionedThroughput(new ProvisionedThroughput(1L, 1L));

    TableUtils.createTableIfNotExists(dynamoDB, createTableRequest);
  }

  /** DynamoDB에선 테이블 row를 날리는게 더 비용이 커서 테이블을 그냥 다시 만든다 */
  @AfterEach
  void after() {
    TableUtils.deleteTableIfExists(
        dynamoDB, dynamoDBMapper.generateDeleteTableRequest(Order.class));
  }

  @Test
  @DisplayName("Order가 정상적으로 DynamoDB에 저장된다")
  void saveOrder() {
    // given
    Long memberId = 1L;
    String orderId = OrderNoGenerator.generate(1L);
    Order order = createOrder(orderId, 100000, "나이키 신발 외 2건", SINGLE);
    // when
    orderDynamoRepository.save(order);

    // then
    Order savedOrder = orderDynamoRepository.findAll().iterator().next();
    assertThat(savedOrder.getId()).isEqualTo(orderId);
    assertThat(savedOrder.getOrderPrice()).isEqualTo(100000);
    System.out.println(savedOrder.getStatus());
  }

  private Order createOrder(
      String orderId, Integer orderPrice, String productsName, OrderType type) {
    return Order.builder()
        .id(orderId)
        .orderPrice(orderPrice)
        .productsName(productsName)
        .type(type)
        .build();
  }
}

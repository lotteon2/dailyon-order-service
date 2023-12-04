package com.dailyon.orderservice.dynamodb.entity;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.dailyon.orderservice.domain.order.entity.enums.OrderType;
import lombok.*;

import javax.persistence.Id;

import static com.dailyon.orderservice.dynamodb.entity.Order.DYNAMO_TABLE_NAME;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@DynamoDBTable(tableName = DYNAMO_TABLE_NAME)
public class Order {

  public static final String DYNAMO_TABLE_NAME = "order";

  @Id @DynamoDBHashKey private String id;

  @DynamoDBAttribute(attributeName = "member_id")
  private Long memberId;

  @DynamoDBAttribute(attributeName = "type")
  private String type;

  @DynamoDBAttribute(attributeName = "products_name")
  private String productsName;

  @DynamoDBAttribute(attributeName = "order_price")
  private Integer orderPrice;

  @DynamoDBAttribute(attributeName = "status")
  private String status = "PENDING";

  @Builder
  private Order(String id, Long memberId, OrderType type, String productsName, Integer orderPrice) {
    this.id = id;
    this.memberId = memberId;
    this.type = type.name();
    this.productsName = productsName;
    this.orderPrice = orderPrice;
  }
}

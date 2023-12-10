package com.dailyon.orderservice.domain.torder.entity;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverted;
import com.dailyon.orderservice.config.DynamoDbConfig;
import com.dailyon.orderservice.domain.order.entity.enums.OrderStatus;
import lombok.*;

import javax.persistence.Id;
import java.time.LocalDateTime;
import java.util.List;

import static com.dailyon.orderservice.domain.torder.entity.TOrder.DYNAMO_TABLE_NAME;

@Getter
@Setter
@ToString
@NoArgsConstructor
@DynamoDBTable(tableName = DYNAMO_TABLE_NAME)
public class TOrder {

  public static final String DYNAMO_TABLE_NAME = "orders";

  @Id @DynamoDBHashKey private String id;

  @DynamoDBAttribute(attributeName = "member_id")
  private Long memberId;

  @DynamoDBAttribute(attributeName = "type")
  private String type;

  @DynamoDBAttribute(attributeName = "used_points")
  private int usedPoints;

  @DynamoDBAttribute(attributeName = "delivery_fee")
  private int deliveryFee;

  @DynamoDBAttribute(attributeName = "total_coupon_discount_price")
  private int totalCouponDiscountPrice;

  @DynamoDBAttribute(attributeName = "status")
  private String status = OrderStatus.PENDING.name();

  @DynamoDBAttribute(attributeName = "order_details")
  private List<TOrderDetail> orderDetails;

  @DynamoDBAttribute(attributeName = "created_at")
  @DynamoDBTypeConverted(converter = DynamoDbConfig.LocalDateTimeConverter.class)
  private LocalDateTime createdAt = LocalDateTime.now();

  @Builder
  public TOrder(
      String id,
      Long memberId,
      String type,
      int usedPoints,
      int deliveryFee,
      int totalCouponDiscountPrice,
      List<TOrderDetail> orderDetails) {
    this.id = id;
    this.memberId = memberId;
    this.type = type;
    this.usedPoints = usedPoints;
    this.deliveryFee = deliveryFee;
    this.totalCouponDiscountPrice = totalCouponDiscountPrice;
    this.orderDetails = orderDetails;
  }

  public Integer calculateTotalAmount() {
    return orderDetails.stream().mapToInt(TOrderDetail::getOrderPrice).sum();
  }

  public Integer calculateTotalCouponDiscountPrice() {
    return orderDetails.stream().mapToInt(TOrderDetail::getCouponDiscountPrice).sum();
  }
}

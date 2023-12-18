package com.dailyon.orderservice.domain.torder.entity;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDocument;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverted;
import com.dailyon.orderservice.config.DynamoDbConfig;
import com.dailyon.orderservice.domain.delivery.entity.Delivery;
import com.dailyon.orderservice.domain.delivery.entity.enums.DeliveryStatus;
import com.dailyon.orderservice.domain.order.entity.Order;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@DynamoDBDocument
public class TDelivery {

  @DynamoDBAttribute(attributeName = "order_no")
  private String orderNo;

  @DynamoDBAttribute(attributeName = "status")
  private String status = DeliveryStatus.BEFORE_DELIVERY.name();

  @DynamoDBAttribute(attributeName = "receiver")
  private String receiver;

  @DynamoDBAttribute(attributeName = "post_code")
  private String postCode;

  @DynamoDBAttribute(attributeName = "road_address")
  private String roadAddress;

  @DynamoDBAttribute(attributeName = "detail_address")
  private String detailAddress;

  @DynamoDBAttribute(attributeName = "phone_number")
  private String phoneNumber;

  @DynamoDBAttribute(attributeName = "created_at")
  @DynamoDBTypeConverted(converter = DynamoDbConfig.LocalDateTimeConverter.class)
  private LocalDateTime createdAt = LocalDateTime.now();

  @DynamoDBAttribute(attributeName = "updated_at")
  @DynamoDBTypeConverted(converter = DynamoDbConfig.LocalDateTimeConverter.class)
  private LocalDateTime updatedAt;

  @Builder
  private TDelivery(
      String orderNo,
      String receiver,
      String postCode,
      String roadAddress,
      String detailAddress,
      String phoneNumber) {
    this.orderNo = orderNo;
    this.receiver = receiver;
    this.postCode = postCode;
    this.roadAddress = roadAddress;
    this.detailAddress = detailAddress;
    this.phoneNumber = phoneNumber;
  }

  public Delivery from(Order order) {
    return Delivery.builder()
        .order(order)
        .receiver(receiver)
        .postCode(postCode)
        .roadAddress(roadAddress)
        .detailAddress(detailAddress)
        .phoneNumber(phoneNumber)
        .build();
  }
}

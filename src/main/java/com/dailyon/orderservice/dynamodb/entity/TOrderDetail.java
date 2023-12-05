package com.dailyon.orderservice.dynamodb.entity;

import com.amazonaws.services.dynamodbv2.datamodeling.*;
import com.dailyon.orderservice.domain.order.entity.enums.OrderDetailStatus;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@DynamoDBDocument
public class TOrderDetail {

  @DynamoDBHashKey(attributeName = "order_detail_id")
  private String id = UUID.randomUUID().toString();

  @DynamoDBAttribute(attributeName = "order_id")
  private String orderId;

  @DynamoDBAttribute(attributeName = "product_id")
  private Long productId;

  @DynamoDBAttribute(attributeName = "product_size_id")
  private Long productSizeId;

  @DynamoDBAttribute(attributeName = "coupon_info_id")
  private Long couponInfoId;

  @DynamoDBAttribute(attributeName = "product_name")
  private String productName;

  @DynamoDBAttribute(attributeName = "product_quantity")
  private Integer productQuantity;

  @DynamoDBAttribute(attributeName = "product_size")
  private String productSize;

  @DynamoDBAttribute(attributeName = "product_gender")
  private String productGender;

  @DynamoDBAttribute(attributeName = "product_iml_url")
  private String productImgUrl;

  @DynamoDBAttribute(attributeName = "order_price")
  private Integer OrderPrice;

  @DynamoDBAttribute(attributeName = "coupon_name")
  private String couponName;

  @DynamoDBAttribute(attributeName = "coupon_discount_price")
  private Integer couponDiscountPrice;

  @DynamoDBAttribute(attributeName = "status")
  private String status = OrderDetailStatus.PENDING.name();

  @DynamoDBTyped(DynamoDBMapperFieldModel.DynamoDBAttributeType.BOOL) //빼면 0으로 들어감, 넣으면 false로 들어감
  @DynamoDBAttribute(attributeName = "review_check")
  private boolean reviewCheck;

  @Builder
  private TOrderDetail(
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
    this.orderId = orderId;
    this.productId = productId;
    this.productSizeId = productSizeId;
    this.couponInfoId = couponInfoId;
    this.productName = productName;
    this.productQuantity = productQuantity;
    this.productSize = productSize;
    this.productGender = productGender;
    this.productImgUrl = productImgUrl;
    this.OrderPrice = orderPrice;
    this.couponName = couponName;
    this.couponDiscountPrice = couponDiscountPrice;
  }
}

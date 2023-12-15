package com.dailyon.orderservice.domain.torder.entity;

import com.amazonaws.services.dynamodbv2.datamodeling.*;
import com.dailyon.orderservice.domain.order.entity.Order;
import com.dailyon.orderservice.domain.order.entity.OrderDetail;
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

  @DynamoDBAttribute(attributeName = "order_no")
  private String orderNo;

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
  private Integer orderPrice;

  @DynamoDBAttribute(attributeName = "coupon_name")
  private String couponName;

  @DynamoDBAttribute(attributeName = "coupon_discount_price")
  private Integer couponDiscountPrice;

  @DynamoDBAttribute(attributeName = "status")
  private String status = OrderDetailStatus.BEFORE_DELIVERY.name();

  @DynamoDBTyped(DynamoDBMapperFieldModel.DynamoDBAttributeType.BOOL) // 빼면 0으로 들어감, 넣으면 false로 들어감
  @DynamoDBAttribute(attributeName = "review_check")
  private boolean reviewCheck;

  @Builder
  private TOrderDetail(
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
    this.orderNo = orderNo;
    this.productId = productId;
    this.productSizeId = productSizeId;
    this.couponInfoId = couponInfoId;
    this.productName = productName;
    this.productQuantity = productQuantity;
    this.productSize = productSize;
    this.productGender = productGender;
    this.productImgUrl = productImgUrl;
    this.orderPrice = orderPrice;
    this.couponName = couponName;
    this.couponDiscountPrice = couponDiscountPrice;
  }

  public OrderDetail toEntity(Order order) {
    return OrderDetail.builder()
        .order(order)
        .orderNo(orderNo)
        .orderDetailNo(id)
        .productSize(productSize)
        .couponDiscountPrice(couponDiscountPrice)
        .couponInfoId(couponInfoId)
        .couponName(couponName)
        .orderPrice(orderPrice)
        .productGender(productGender)
        .productId(productId)
        .productImgUrl(productImgUrl)
        .productName(productName)
        .productQuantity(productQuantity)
        .productSizeId(productSizeId)
        .build();
  }
}

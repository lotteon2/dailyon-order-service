package com.dailyon.orderservice.domain.order.entity;

import com.dailyon.orderservice.domain.common.BaseEntity;
import com.dailyon.orderservice.domain.order.entity.enums.OrderDetailStatus;
import com.dailyon.orderservice.domain.order.exception.CancellationNotAllowedException;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import static com.dailyon.orderservice.domain.order.entity.enums.OrderDetailStatus.BEFORE_DELIVERY;
import static com.dailyon.orderservice.domain.order.entity.enums.OrderDetailStatus.CANCEL;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicInsert
@BatchSize(size = 100)
@Entity
@Table(indexes = @Index(name = "idx_order_detail_no", columnList = "orderDetailNo", unique = true))
public class OrderDetail extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "order_id", nullable = false)
  private Order order;

  @NotNull private String orderNo;

  @NotNull private String orderDetailNo;

  @NotNull private Long productId;
  @NotNull private Long productSizeId;

  @Column(nullable = true)
  private Long couponInfoId;

  @NotNull private String productName;
  @NotNull private Integer productQuantity;
  @NotNull private String productSize;
  @NotNull private String productGender;
  @NotNull private String productImgUrl;
  @NotNull private Integer orderPrice;

  @Column(nullable = true)
  private String couponName;

  @Column(nullable = true)
  private Integer couponDiscountPrice;

  @NotNull
  @Enumerated(EnumType.STRING)
  private OrderDetailStatus status = BEFORE_DELIVERY;

  @Column(nullable = false, columnDefinition = "boolean default false")
  private Boolean reviewCheck;

  @Builder
  private OrderDetail(
      Order order,
      String orderNo,
      String orderDetailNo,
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
    this.order = order;
    this.orderNo = orderNo;
    this.orderDetailNo = orderDetailNo;
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

  public void cancel() {
    if (!status.equals(BEFORE_DELIVERY)) {
      throw new CancellationNotAllowedException();
    }
    this.status = CANCEL;
  }
}

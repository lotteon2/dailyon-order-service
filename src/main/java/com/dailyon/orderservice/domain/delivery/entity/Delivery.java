package com.dailyon.orderservice.domain.delivery.entity;

import com.dailyon.orderservice.domain.common.BaseEntity;
import com.dailyon.orderservice.domain.delivery.entity.enums.DeliveryStatus;
import com.dailyon.orderservice.domain.order.entity.Order;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicInsert
@Entity
public class Delivery extends BaseEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "order_id", nullable = false)
  private Order order;

  @NotNull
  @Enumerated(EnumType.STRING)
  private DeliveryStatus status = DeliveryStatus.READY;

  @NotNull private String receiver;

  @NotNull private String postCode;

  @NotNull private String roadAddress;

  @NotNull private String detailAddress;

  @Column(nullable = true)
  private String phoneNumber;

  @Builder
  private Delivery(
      Order order,
      DeliveryStatus status,
      String receiver,
      String postCode,
      String roadAddress,
      String detailAddress,
      String phoneNumber) {
    this.order = order;
    this.status = status;
    this.receiver = receiver;
    this.postCode = postCode;
    this.roadAddress = roadAddress;
    this.detailAddress = detailAddress;
    this.phoneNumber = phoneNumber;
  }
}

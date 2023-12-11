package com.dailyon.orderservice.domain.order.entity;

import com.dailyon.orderservice.domain.common.BaseEntity;
import com.dailyon.orderservice.domain.order.entity.enums.OrderStatus;
import com.dailyon.orderservice.domain.order.entity.enums.OrderType;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicInsert
@Entity(name = "orders")
public class Order extends BaseEntity {
  @Id private String id;

  @NotNull private Long memberId;

  @NotNull
  @Enumerated(EnumType.STRING)
  private OrderType type;

  @NotNull
  @Enumerated(EnumType.STRING)
  private OrderStatus status = OrderStatus.COMPLETED;

  @Column(nullable = false, columnDefinition = "boolean default false")
  private Boolean isDeleted;

  @OneToMany(mappedBy = "order", fetch = FetchType.LAZY)
  private List<OrderDetail> orderDetails;

  @Builder
  private Order(String id, Long memberId, OrderType type) {
    this.id = id;
    this.memberId = memberId;
    this.type = type;
  }

  public void add(List<OrderDetail> orderDetails) {
    this.orderDetails = orderDetails;
  }
}

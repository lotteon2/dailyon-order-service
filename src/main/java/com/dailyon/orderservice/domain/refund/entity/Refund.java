package com.dailyon.orderservice.domain.refund.entity;

import com.dailyon.orderservice.domain.common.BaseEntity;
import com.dailyon.orderservice.domain.order.entity.Order;
import com.dailyon.orderservice.domain.order.entity.OrderDetail;
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
public class Refund extends BaseEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "order_id")
  private Order order;

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "order_detail_id", nullable = false)
  private OrderDetail orderDetail;

  @NotNull private Integer price;

  @Column(name = "points", columnDefinition = "int default 0")
  private int points;

  @Builder
  private Refund(Order order, OrderDetail orderDetail, Integer price, int points) {
    this.order = order;
    this.orderDetail = orderDetail;
    this.price = price;
    this.points = points;
  }
}

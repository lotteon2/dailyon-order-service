package com.dailyon.orderservice.domain.order.entity;

import com.dailyon.orderservice.domain.common.BaseEntity;
import com.dailyon.orderservice.domain.order.entity.enums.GiftStatus;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "gifts")
public class Gift extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private Long receiverId;

  private String orderNo;

  @Enumerated(EnumType.STRING)
  private GiftStatus status = GiftStatus.PENDING;

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "order_id", nullable = true, unique = true)
  private Order order;

  @Column(nullable = false)
  private String receiverName;

  @Column(nullable = false)
  private String senderName;

  @Builder
  private Gift(Long receiverId, String orderNo, String senderName, String receiverName) {
    this.receiverId = receiverId;
    this.orderNo = orderNo;
    this.receiverName = receiverName;
    this.senderName = senderName;
  }

  public void add(Order order) {
    this.order = order;
  }

  public void orderComplete() {
    this.status = GiftStatus.ORDER_COMPLETE;
  }
}

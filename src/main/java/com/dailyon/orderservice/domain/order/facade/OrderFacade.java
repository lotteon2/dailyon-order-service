package com.dailyon.orderservice.domain.order.facade;

import com.dailyon.orderservice.domain.delivery.service.DeliveryService;
import com.dailyon.orderservice.domain.delivery.service.request.DeliveryServiceRequest;
import com.dailyon.orderservice.domain.order.entity.Gift;
import com.dailyon.orderservice.domain.order.entity.Order;
import com.dailyon.orderservice.domain.order.entity.OrderDetail;
import com.dailyon.orderservice.domain.order.entity.enums.OrderType;
import com.dailyon.orderservice.domain.order.facade.response.OrderDetailResponse;
import com.dailyon.orderservice.domain.order.facade.response.OrderPageResponse;
import com.dailyon.orderservice.domain.order.kafka.event.OrderEventProducer;
import com.dailyon.orderservice.domain.order.kafka.event.dto.RefundDTO;
import com.dailyon.orderservice.domain.order.service.GiftService;
import com.dailyon.orderservice.domain.order.service.OrderService;
import com.dailyon.orderservice.domain.order.sqs.OrderSqsProducer;
import com.dailyon.orderservice.domain.order.sqs.dto.RawNotificationData;
import com.dailyon.orderservice.domain.order.sqs.dto.SQSNotificationDto;
import com.dailyon.orderservice.domain.refund.entity.Refund;
import com.dailyon.orderservice.domain.refund.service.RefundService;
import com.dailyon.orderservice.domain.torder.entity.TOrder;
import com.dailyon.orderservice.domain.torder.service.TOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.dailyon.orderservice.domain.order.entity.enums.OrderType.GIFT;
import static com.dailyon.orderservice.domain.order.sqs.OrderSqsProducer.*;
import static com.dailyon.orderservice.domain.order.sqs.dto.RawNotificationData.forGiftReceived;
import static com.dailyon.orderservice.domain.order.sqs.dto.RawNotificationData.forOrderComplete;
import static com.dailyon.orderservice.domain.order.sqs.dto.SQSNotificationDto.of;

@Service
@RequiredArgsConstructor
public class OrderFacade {
  private final OrderService orderService;
  private final TOrderService tOrderService;
  private final DeliveryService deliveryService;
  private final RefundService refundService;
  private final GiftService giftService;
  private final OrderEventProducer producer;
  private final OrderSqsProducer orderSqsProducer;

  public String orderCreate(String orderNo) {
    TOrder tOrder = tOrderService.getTOrder(orderNo);
    Order order = orderService.createOrder(tOrder);

    if (GIFT.equals(OrderType.valueOf(tOrder.getType()))) {
      Gift gift = giftService.update(order);

      RawNotificationData notificationData = forGiftReceived(gift.getReceiverName());
      SQSNotificationDto notificationDto = of(gift.getReceiverId(), notificationData);
      orderSqsProducer.produce(GIFT_RECEIVED_NOTIFICATION_QUEUE, notificationDto);
    } else {
      deliveryService.createDelivery(DeliveryServiceRequest.from(tOrder.getDelivery()));

      RawNotificationData notificationData =
          forOrderComplete(order.getOrderNo(), order.getTotalAmount());
      SQSNotificationDto notificationDto = of(order.getMemberId(), notificationData);
      orderSqsProducer.produce(ORDER_COMPLETE_NOTIFICATION_QUEUE, notificationDto);
    }
    tOrderService.deleteTOrder(tOrder.getId());
    return tOrder.getId();
  }

  public OrderPageResponse getOrders(Pageable pageable, Long memberId) {
    Page<Order> page = orderService.getOrders(pageable, memberId);
    return OrderPageResponse.from(page);
  }

  public List<OrderDetailResponse> getOrderDetails(String orderNo, Long memberId) {
    List<OrderDetail> orderDetails = orderService.getOrderDetails(orderNo, memberId);
    return OrderDetailResponse.from(orderDetails);
  }

  @Transactional
  public Long cancelOrderDetail(String OrderDetailNo, Long memberId) {
    OrderDetail orderDetail = orderService.cancelOrderDetail(OrderDetailNo, memberId);
    Refund refund = refundService.createRefund(orderDetail);
    producer.createRefund(orderDetail.getOrderDetailNo(), RefundDTO.of(orderDetail, refund));

    RawNotificationData notificationData =
        RawNotificationData.forOrderCanceled(
            orderDetail.getOrderPrice(),
            orderDetail.getProductName(),
            orderDetail.getProductQuantity());
    SQSNotificationDto notificationDto = of(memberId, notificationData);
    orderSqsProducer.produce(ORDER_CANCELED_NOTIFICATION_QUEUE, notificationDto);
    return refund.getId();
  }

  public List<Long> getMostSoldProductIds() {
    return orderService.getMostSoldProductIds();
  }
}

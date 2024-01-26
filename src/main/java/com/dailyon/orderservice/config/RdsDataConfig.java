package com.dailyon.orderservice.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateProperties;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import static com.dailyon.orderservice.config.RdsDataConfig.*;

@RequiredArgsConstructor
@Configuration
@EnableJpaRepositories(
    basePackages = {
      RDS_ORDER_DOMAIN_PACKAGE,
      RDS_DELIVERY_DOMAIN_PACKAGE,
      RDS_REFUND_DOMAIN_PACKAGE,
      RDS_GIFT_DOMAIN_PACKAGE
    }) // JpaRepository 패키지 위치 등록
@EnableConfigurationProperties({JpaProperties.class, HibernateProperties.class})
public class RdsDataConfig {

  static final String RDS_ORDER_DOMAIN_PACKAGE = "com.dailyon.orderservice.domain.order.repository";
  static final String RDS_DELIVERY_DOMAIN_PACKAGE =
      "com.dailyon.orderservice.domain.delivery.repository";
  static final String RDS_REFUND_DOMAIN_PACKAGE =
      "com.dailyon.orderservice.domain.refund.repository";
  static final String RDS_GIFT_DOMAIN_PACKAGE = "com.dailyon.orderservice.domain.gift.repository";
}

package com.dailyon.orderservice.dynamodb;

import com.dailyon.orderservice.dynamodb.entity.Order;
import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;

@EnableScan
public interface OrderDynamoRepository extends CrudRepository<Order, String> {}

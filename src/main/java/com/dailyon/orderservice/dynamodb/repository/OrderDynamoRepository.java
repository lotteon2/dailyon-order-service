package com.dailyon.orderservice.dynamodb.repository;

import com.dailyon.orderservice.dynamodb.entity.TOrder;
import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;

@EnableScan
public interface OrderDynamoRepository extends CrudRepository<TOrder, String> {}

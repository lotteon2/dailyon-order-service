package com.dailyon.orderservice.domain.torder.repository;

import com.dailyon.orderservice.domain.torder.entity.TOrder;
import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;

@EnableScan
public interface OrderDynamoRepository extends CrudRepository<TOrder, String> {}

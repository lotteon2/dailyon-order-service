package com.dailyon.orderservice.domain.order.dynamo.repository;

import com.dailyon.orderservice.domain.order.dynamo.document.TOrder;
import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

@EnableScan
public interface OrderDynamoRepository extends CrudRepository<TOrder, String> {

  List<TOrder> findByMemberIdOrderByCreatedAtDesc(Long memberId);
}

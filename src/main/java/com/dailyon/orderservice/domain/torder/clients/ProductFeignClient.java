package com.dailyon.orderservice.domain.torder.clients;

import com.dailyon.orderservice.config.DefaultFeignConfig;
import dailyon.domain.order.clients.ProductDTO;
import dailyon.domain.order.clients.ProductDTO.OrderProductParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(
    name = "product-service",
    url = "${endpoint.product-service}",
    configuration = DefaultFeignConfig.class)
public interface ProductFeignClient {

  @PostMapping("/clients/products/orders")
  ProductDTO.OrderProductListDTO getOrderProducts(
      @RequestBody List<OrderProductParam> productParams);
}

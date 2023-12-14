package com.dailyon.orderservice.domain.torder.clients;

import com.dailyon.orderservice.config.DefaultFeignConfig;
import com.dailyon.orderservice.domain.torder.clients.dto.ProductDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.List;

@FeignClient(name = "product-service", url = "${endpoint.product-service}",configuration = DefaultFeignConfig.class)
public interface ProductFeignClient {

  @PostMapping("/clients/products/orders")
  ProductDTO.OrderProductListDTO getOrderProducts(
      @RequestBody List<ProductDTO.OrderProductParam> productParams);
}

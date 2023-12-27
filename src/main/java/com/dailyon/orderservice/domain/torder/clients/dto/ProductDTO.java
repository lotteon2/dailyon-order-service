package com.dailyon.orderservice.domain.torder.clients.dto;

import lombok.*;

import java.util.List;

public class ProductDTO {

  @Getter
  @Builder
  @ToString
  public static class OrderProductParam {
    private final Long productId;
    private final Long sizeId;
  }

  @Getter
  @NoArgsConstructor
  @AllArgsConstructor
  public static class OrderProductListDTO {
    private List<OrderProductDTO> response;

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class OrderProductDTO {
      private Long productId;
      private String productName;
      private Integer stock;
      private Integer price;
      private String gender;
      private String imgUrl;
      private Long sizeId;
      private String sizeName;
      private Long categoryId;
    }
  }
}

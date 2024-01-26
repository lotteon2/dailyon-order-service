package com.dailyon.orderservice.domain.order.clients.dto;

import dailyon.domain.order.clients.ProductDTO.OrderProductListDTO.OrderProductDTO;
import lombok.*;

import java.util.List;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class AuctionProductDTO {
  private Long productId;
  private String productName;
  private Integer stock;
  private Integer price;
  private String gender;
  private String imgUrl;
  private Long sizeId;
  private String sizeName;
  private boolean isWinner;
  private Long orderPrice;
  private Long categoryId;

  public List<OrderProductDTO> createOrderProducts() {
    return List.of(
        OrderProductDTO.builder()
            .productId(productId)
            .productName(productName)
            .stock(stock)
            .price(price)
            .gender(gender)
            .imgUrl(imgUrl)
            .sizeId(sizeId)
            .sizeName(sizeName)
            .build());
  }
}

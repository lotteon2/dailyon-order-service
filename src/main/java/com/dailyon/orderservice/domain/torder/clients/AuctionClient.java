package com.dailyon.orderservice.domain.torder.clients;

import com.dailyon.orderservice.domain.torder.clients.dto.AuctionProductDTO;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class AuctionClient {

  private final WebClient webClient;

  public AuctionClient(WebClient webClient) {
    this.webClient = webClient;
  }

  public AuctionProductDTO getAuctionProductInfo(Long memberId, String auctionId) {
    return webClient
        .get()
        .uri(
            uriBuilder ->
                uriBuilder.path("/clients/auction-histories/{auctionId}")
                        .build(auctionId))
        .header("memberId", memberId.toString())
        .retrieve()
        .bodyToMono(AuctionProductDTO.class)
        .block();
  }
}

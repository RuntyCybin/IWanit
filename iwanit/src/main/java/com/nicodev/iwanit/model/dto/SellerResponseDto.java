package com.nicodev.iwanit.model.dto;

import java.util.List;

public record SellerResponseDto(Long id, String name,
    String email, String phoneNumber, Long userId, List<OfferResponseDto> offers) {

  public SellerResponseDto {
    // defensive copy: List.copyOf also rejects null elements and is immutable
    offers = offers == null ? List.of() : List.copyOf(offers);
  }

}

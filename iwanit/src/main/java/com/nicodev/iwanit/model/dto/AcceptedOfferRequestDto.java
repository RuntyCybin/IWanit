package com.nicodev.iwanit.model.dto;

public record AcceptedOfferRequestDto(Long offerId, Long articleId) {

  public AcceptedOfferRequestDto {
    if (offerId == null) {
      throw new IllegalArgumentException("Offer ID must not be null");
    }
    if (articleId == null) {
      throw new IllegalArgumentException("Article ID must not be null");
    }
  }

}

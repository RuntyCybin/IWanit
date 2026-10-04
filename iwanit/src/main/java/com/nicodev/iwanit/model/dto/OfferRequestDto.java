package com.nicodev.iwanit.model.dto;

import java.math.BigDecimal;

public record OfferRequestDto(String name, String description,
    BigDecimal price, Long articleId, Long sellerId) {

  public OfferRequestDto {
    if (name == null || name.isBlank()) {
      throw new IllegalArgumentException("Name must not be null or blank");
    }
    if (description == null || description.isBlank()) {
      throw new IllegalArgumentException("Description must not be null or blank");
    }
    if (price == null || price.signum() < 0) {
      throw new IllegalArgumentException("Price must not be null and must be non-negative");
    }
    if (articleId == null) {
      throw new IllegalArgumentException("Article ID must not be null");
    }
    if (sellerId == null) {
      throw new IllegalArgumentException("Seller ID must not be null");
    }
  }

}

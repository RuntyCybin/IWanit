package com.nicodev.iwanit.model.dto;

import java.math.BigDecimal;
import java.util.Objects;

public record OfferRequestDto(String name, String description,
    BigDecimal price, Long articleId, Long sellerId) {

  public OfferRequestDto {
    Objects.requireNonNull(name, "Name must not be null");
    Objects.requireNonNull(description, "Description must not be null");
    Objects.requireNonNull(price, "Price must not be null");
    Objects.requireNonNull(articleId, "Article ID must not be null");
    Objects.requireNonNull(sellerId, "Seller ID must not be null");

    if (price.signum() < 0) {
      throw new IllegalArgumentException("Price must be non-negative");
    }
  }

}

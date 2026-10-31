package com.nicodev.iwanit.model.dto;

import java.math.BigDecimal;

public record OfferResponseDto(
    Long id,
    String name,
    String description,
    BigDecimal price,
    Long articleId,
    Long sellerId) {

}

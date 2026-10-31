package com.nicodev.iwanit.model.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record AcceptedOfferResponseDto(
    Long id,
    Long offerId,
    Long articleId,
    Long sellerId,
    BigDecimal price,
    LocalDateTime acceptedAt) {

}

package com.nicodev.iwanit.model.dto;

import java.time.LocalDateTime;

public record AcceptedOfferResponseDto(
    Long id,
    Long offerId,
    Long articleId,
    Long sellerId,
    Double price,
    LocalDateTime acceptedAt) {

}

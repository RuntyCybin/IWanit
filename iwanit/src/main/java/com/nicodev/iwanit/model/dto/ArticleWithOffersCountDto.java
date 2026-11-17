package com.nicodev.iwanit.model.dto;

import java.math.BigDecimal;

public record ArticleWithOffersCountDto(Long id, String name, String description, BigDecimal price, Long buyerId,
    Long offersCount) {
}

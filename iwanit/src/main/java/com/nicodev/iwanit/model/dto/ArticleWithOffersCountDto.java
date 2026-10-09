package com.nicodev.iwanit.model.dto;

public record ArticleWithOffersCountDto(Long id, String name, String description, Double price, Long buyerId,
    Long sellerId, Long offersCount) {
}

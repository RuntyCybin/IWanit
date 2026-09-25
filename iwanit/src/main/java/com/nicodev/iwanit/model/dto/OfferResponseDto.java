package com.nicodev.iwanit.model.dto;

public record OfferResponseDto(Long id, String name, String description, Double price, Long articleId, Long sellerId) {

}

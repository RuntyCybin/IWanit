package com.nicodev.iwanit.model.dto;

public record ArticleRequestDto(String title, String content, Double price, Long buyerId) {

  public ArticleRequestDto {
    if (title == null || title.isBlank()) {
      throw new IllegalArgumentException("Title must not be null or blank");
    }
    if (content == null || content.isBlank()) {
      throw new IllegalArgumentException("Content must not be null or blank");
    }
    if (price == null || price < 0) {
      throw new IllegalArgumentException("Price must not be null and must be non-negative");
    }
    if (buyerId == null) {
      throw new IllegalArgumentException("Buyer ID must not be null");
    }
  }

}

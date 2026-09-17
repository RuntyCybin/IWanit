package com.nicodev.iwanit.model.dto;

import java.util.List;

public record BuyerResponseDto(Long id, String name, String email, String phoneNumber, Long userId,
    List<ArticleResponseDto> articles) {

  public BuyerResponseDto {
    articles = articles == null ? List.of() : List.copyOf(articles);
  }

}

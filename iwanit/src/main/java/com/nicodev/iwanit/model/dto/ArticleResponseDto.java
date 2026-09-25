package com.nicodev.iwanit.model.dto;

public record ArticleResponseDto(Long id, String title, String content, Double price, Long buyer, Long seller) {

  public ArticleResponseDto(Long id, String title, String content, Double price, Long buyer) {
    this(id, title, content, price, buyer, null);
  }

}

package com.nicodev.iwanit.model.dto;

import com.nicodev.iwanit.model.Buyer;
import com.nicodev.iwanit.model.Seller;

public record ArticleResponseDto(Long id, String title, String content, Double price, Buyer buyer, Seller seller) {

  public ArticleResponseDto(Long id, String title, String content, Double price, Buyer buyer) {
    this(id, title, content, price, buyer, null);
  }

}

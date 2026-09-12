package com.nicodev.iwanit.model.mapper;

import org.springframework.stereotype.Component;

import com.nicodev.iwanit.model.Article;
import com.nicodev.iwanit.model.Buyer;
import com.nicodev.iwanit.model.dto.ArticleRequestDto;
import com.nicodev.iwanit.model.dto.ArticleResponseDto;

@Component
public class ArticleMapper {

  public ArticleResponseDto toArticleResponseDto(Article article) {
    return new ArticleResponseDto(
        article.getId(),
        article.getName(),
        article.getDescription(),
        article.getPrice(),
        article.getBuyer());
  }

  public Article mapToArticle(ArticleRequestDto articleRequestDto, Buyer buyer) {
    return new Article(
        articleRequestDto.title(),
        articleRequestDto.content(),
        articleRequestDto.price(),
        buyer);
  }

  public boolean isArticleRequestDtoValid(ArticleRequestDto articleRequestDto) {
    return articleRequestDto != null &&
        articleRequestDto.title() != null && !articleRequestDto.title().trim().isEmpty() &&
        articleRequestDto.content() != null && !articleRequestDto.content().trim().isEmpty() &&
        articleRequestDto.price() != null && articleRequestDto.price() > 0 &&
        articleRequestDto.buyerId() != null && articleRequestDto.buyerId() > 0;
  }
}

package com.nicodev.iwanit.service;

import java.util.List;

import com.nicodev.iwanit.model.dto.ArticleRequestDto;
import com.nicodev.iwanit.model.dto.ArticleResponseDto;

public interface ArticleService {

  ArticleResponseDto createArticle(ArticleRequestDto articleRequestDto);

  ArticleResponseDto updateArticle(ArticleRequestDto articleRequestDto, Long id);

  void deleteArticle(Long id);

  ArticleResponseDto getArticleById(Long id);

  List<ArticleResponseDto> getAllBuyerArticles(Long buyerId);
}

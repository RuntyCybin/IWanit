package com.nicodev.iwanit.service;

import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Service;

import com.nicodev.iwanit.exception.ArticleAlreadyExistException;
import com.nicodev.iwanit.exception.ArticleInvalidRequestException;
import com.nicodev.iwanit.exception.ArticleNotSavedException;
import com.nicodev.iwanit.exception.BuyerNotFoundException;
import com.nicodev.iwanit.model.Article;
import com.nicodev.iwanit.model.Buyer;
import com.nicodev.iwanit.model.dto.ArticleRequestDto;
import com.nicodev.iwanit.model.dto.ArticleResponseDto;
import com.nicodev.iwanit.model.mapper.ArticleMapper;
import com.nicodev.iwanit.repository.ArticleRepository;
import com.nicodev.iwanit.repository.BuyerRepository;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ArticleServiceImpl implements ArticleService {

  private final ArticleRepository articleRepository;
  private final BuyerRepository buyerRepository;
  private final ArticleMapper articleMapper;

  @Override
  @Transactional
  public ArticleResponseDto createArticle(ArticleRequestDto articleRequestDto) {
    Objects.requireNonNull(articleRequestDto, "ArticleRequestDto must not be null");

    if (!articleMapper.isArticleRequestDtoValid(articleRequestDto)) {
      throw new ArticleInvalidRequestException("Invalid ArticleRequestDto: " + articleRequestDto);
    }

    articleRepository.findByName(articleRequestDto.title())
        .ifPresent(article -> {
          throw new ArticleAlreadyExistException(articleRequestDto.title());
        });

    try {
      Buyer buyer = buyerRepository.findById(articleRequestDto.buyerId())
          .orElseThrow(() -> new BuyerNotFoundException(articleRequestDto.buyerId(),
              "Buyer not found with ID: " + articleRequestDto.buyerId()));

      var article = articleMapper.mapToArticle(articleRequestDto, buyer);

      var savedArticle = articleRepository.save(article);
      return articleMapper.toArticleResponseDto(savedArticle);
    } catch (Exception e) {
      throw new ArticleNotSavedException(e.getMessage());
    }
  }

  @Override
  @Transactional
  public ArticleResponseDto updateArticle(ArticleRequestDto articleRequestDto, Long id) {
    Objects.requireNonNull(articleRequestDto, "ArticleRequestDto must not be null");
    Objects.requireNonNull(id, "Article ID must not be null");

    Article article = articleRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Article not found with ID: " + id));

    article.setName(articleRequestDto.title());
    article.setDescription(articleRequestDto.content());
    article.setPrice(articleRequestDto.price());

    Buyer buyer = buyerRepository.findById(articleRequestDto.buyerId())
        .orElseThrow(() -> new BuyerNotFoundException(articleRequestDto.buyerId(),
            "Buyer not found with ID: " + articleRequestDto.buyerId()));
    article.setBuyer(buyer);

    try {
      Article savedArticle = articleRepository.save(article);
      return articleMapper.toArticleResponseDto(savedArticle);
    } catch (Exception e) {
      throw new ArticleNotSavedException(e.getMessage());
    }
  }

  @Override
  @Transactional
  public void deleteArticle(Long id) {
    Objects.requireNonNull(id, "Article ID must not be null");

    articleRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Article not found with ID: " + id));

    try {
      articleRepository.deleteById(id);
    } catch (Exception e) {
      // TODO: create an exception class for failed article deletion and throw it here
      throw new RuntimeException("Failed to delete article with ID: " + id, e);
    }
  }

  @Override
  public ArticleResponseDto getArticleById(Long id) {
    Objects.requireNonNull(id, "Article ID must not be null");

    return articleRepository.findById(id)
        .map(articleMapper::toArticleResponseDto)
        .orElseThrow(() -> new RuntimeException("Article not found with ID: " + id));
  }

  @Override
  public List<ArticleResponseDto> getAllBuyerArticles(Long buyerId) {
    Objects.requireNonNull(buyerId, "Buyer ID must not be null");

    if (!buyerRepository.existsById(buyerId)) {
      throw new RuntimeException("Buyer not found with ID: " + buyerId);
    }

    return articleRepository.findAllByBuyerId(buyerId).stream()
        .map(articleMapper::toArticleResponseDto)
        .toList();
  }

}

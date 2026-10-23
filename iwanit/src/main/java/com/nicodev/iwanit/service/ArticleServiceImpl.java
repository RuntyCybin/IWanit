package com.nicodev.iwanit.service;

import java.util.List;
import java.util.Objects;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import com.nicodev.iwanit.exception.ArticleAlreadyExistException;
import com.nicodev.iwanit.exception.ArticleInvalidRequestException;
import com.nicodev.iwanit.exception.ArticleNotDeletedException;
import com.nicodev.iwanit.exception.ArticleNotFoundException;
import com.nicodev.iwanit.exception.ArticleNotSavedException;
import com.nicodev.iwanit.exception.BuyerNotFoundException;
import com.nicodev.iwanit.model.Article;
import com.nicodev.iwanit.model.Buyer;
import com.nicodev.iwanit.model.Role;
import com.nicodev.iwanit.model.StatusArticle;
import com.nicodev.iwanit.model.StatusOffer;
import com.nicodev.iwanit.model.dto.ArticleRequestDto;
import com.nicodev.iwanit.model.dto.ArticleResponseDto;
import com.nicodev.iwanit.model.dto.ArticleWithOffersCountDto;
import com.nicodev.iwanit.model.mapper.ArticleMapper;
import com.nicodev.iwanit.repository.ArticleRepository;
import com.nicodev.iwanit.repository.BuyerRepository;
import com.nicodev.iwanit.security.AuthenticatedUser;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ArticleServiceImpl implements ArticleService {

  private final ArticleRepository articleRepository;
  private final BuyerRepository buyerRepository;
  private final ArticleMapper articleMapper;
  private final AuthenticatedUser authenticatedUser;

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

    Buyer buyer = buyerRepository.findById(articleRequestDto.buyerId())
        .orElseThrow(() -> new BuyerNotFoundException(articleRequestDto.buyerId()));

    if (!authenticatedUser.isOwner(Role.BUYER, buyer.getUser())) {
      throw new AccessDeniedException("Articles can only be created as the authenticated buyer");
    }

    var article = articleMapper.mapToArticle(articleRequestDto, buyer);

    var savedArticle = articleRepository.save(article);
    return articleMapper.toArticleResponseDto(savedArticle);
  }

  @Override
  @Transactional
  public ArticleResponseDto updateArticle(ArticleRequestDto articleRequestDto, Long id) {
    Objects.requireNonNull(articleRequestDto, "ArticleRequestDto must not be null");
    Objects.requireNonNull(id, "Article ID must not be null");

    Article article = articleRepository.findByIdAndStatus(id, StatusArticle.AVAILABLE)
        .orElseThrow(() -> new ArticleNotFoundException(id));

    // Only the buyer who owns the article can edit it, and it can't be moved to
    // another buyer
    if (!authenticatedUser.isOwner(Role.BUYER, article.getBuyer().getUser())) {
      throw new AccessDeniedException("Not allowed to modify this article");
    }
    if (!article.getBuyer().getId().equals(articleRequestDto.buyerId())) {
      throw new IllegalArgumentException("The buyer of an article can't be changed");
    }

    article.setName(articleRequestDto.title());
    article.setDescription(articleRequestDto.content());
    article.setPrice(articleRequestDto.price());

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

    Article article = articleRepository.findByIdAndStatus(id, StatusArticle.AVAILABLE)
        .orElseThrow(() -> new ArticleNotFoundException(id));

    if (!authenticatedUser.isOwner(Role.BUYER, article.getBuyer().getUser())) {
      throw new AccessDeniedException("Not allowed to delete this article");
    }

    try {
      articleRepository.deleteById(id);
    } catch (Exception e) {
      throw new ArticleNotDeletedException(id, e);
    }
  }

  @Override
  public ArticleResponseDto getArticleById(Long id) {
    Objects.requireNonNull(id, "Article ID must not be null");

    return articleRepository.findByIdAndStatus(id, StatusArticle.AVAILABLE)
        .map(articleMapper::toArticleResponseDto)
        .orElseThrow(() -> new ArticleNotFoundException(id));
  }

  @Override
  public List<ArticleResponseDto> getAllBuyerArticles(Long buyerId) {
    Objects.requireNonNull(buyerId, "Buyer ID must not be null");

    if (!buyerRepository.existsById(buyerId)) {
      throw new BuyerNotFoundException(buyerId);
    }

    return articleRepository.findAllByBuyerId(buyerId, StatusArticle.AVAILABLE).stream()
        .map(articleMapper::toArticleResponseDto)
        .toList();
  }

  @Override
  public List<ArticleResponseDto> getAllArticles() {
    return this.articleRepository.findAllByStatus(StatusArticle.AVAILABLE).stream()
        .map(articleMapper::toArticleResponseDto)
        .toList();
  }

  @Override
  public List<ArticleWithOffersCountDto> getAllArticlesWithCountByBuyerId() {

    return this.articleRepository.findAllByBuyerIdWithOffersCount(StatusOffer.PENDING);
  }

}

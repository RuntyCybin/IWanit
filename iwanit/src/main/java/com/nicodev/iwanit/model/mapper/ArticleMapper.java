package com.nicodev.iwanit.model.mapper;

import org.springframework.stereotype.Component;

import com.nicodev.iwanit.model.Article;
import com.nicodev.iwanit.model.Buyer;
import com.nicodev.iwanit.model.Offer;
import com.nicodev.iwanit.model.Role;
import com.nicodev.iwanit.model.StatusOffer;
import com.nicodev.iwanit.model.dto.ArticleRequestDto;
import com.nicodev.iwanit.model.dto.ArticleResponseDto;
import com.nicodev.iwanit.model.dto.OfferResponseDto;
import com.nicodev.iwanit.security.AuthenticatedUser;

import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class ArticleMapper {

  private final AuthenticatedUser authenticatedUser;

  public ArticleResponseDto toArticleResponseDto(Article article) {
    return new ArticleResponseDto(
        article.getId(),
        article.getName(),
        article.getDescription(),
        article.getPrice(),
        article.getBuyer().getId(),
        article.getOffers().stream()
            .filter(offer -> offer.getStatus() == StatusOffer.PENDING)
            .filter(offer -> canSeeOffer(article, offer))
            .map(offer -> new OfferResponseDto(
                offer.getId(),
                offer.getName(),
                offer.getDescription(),
                offer.getPrice(),
                article.getId(),
                offer.getSeller().getId()))
            .toList());
  }

  /**
   * Offers are private: the buyer who owns the article sees all of its offers,
   * a seller only sees their own, and anybody else sees none.
   */
  private boolean canSeeOffer(Article article, Offer offer) {
    return authenticatedUser.isOwner(Role.BUYER, article.getBuyer().getUser())
        || authenticatedUser.isOwner(Role.SELLER, offer.getSeller().getUser());
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

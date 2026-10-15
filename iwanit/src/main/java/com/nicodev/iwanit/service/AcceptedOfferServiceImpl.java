package com.nicodev.iwanit.service;

import java.util.Objects;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import com.nicodev.iwanit.exception.AcceptedOfferAlreadyExistException;
import com.nicodev.iwanit.exception.AcceptedOfferNotFoundException;
import com.nicodev.iwanit.exception.ArticleNotAvailableException;
import com.nicodev.iwanit.exception.OfferNotFoundException;
import com.nicodev.iwanit.exception.OfferNotPendingException;
import com.nicodev.iwanit.model.AcceptedOffer;
import com.nicodev.iwanit.model.Article;
import com.nicodev.iwanit.model.Offer;
import com.nicodev.iwanit.model.Role;
import com.nicodev.iwanit.model.StatusArticle;
import com.nicodev.iwanit.model.StatusOffer;
import com.nicodev.iwanit.model.dto.AcceptedOfferRequestDto;
import com.nicodev.iwanit.model.dto.AcceptedOfferResponseDto;
import com.nicodev.iwanit.model.mapper.AcceptedOfferMapper;
import com.nicodev.iwanit.repository.AcceptedOfferRepository;
import com.nicodev.iwanit.repository.OfferRepository;
import com.nicodev.iwanit.security.AuthenticatedUser;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class AcceptedOfferServiceImpl implements AcceptedOfferService {

  private final AcceptedOfferRepository acceptedOfferRepository;
  private final OfferRepository offerRepository;
  private final AcceptedOfferMapper acceptedOfferMapper;
  private final AuthenticatedUser authenticatedUser;

  /**
   * The buyer who owns the article accepts one of its offers.
   * The offer goes to ACCEPTED, the article to BOUGHT and the rest of the
   * pending offers of the article to REJECTED. An article can only have one
   * accepted offer.
   */
  @Override
  @Transactional
  public AcceptedOfferResponseDto createAcceptedOffer(AcceptedOfferRequestDto acceptedOfferRequestDto) {
    Objects.requireNonNull(acceptedOfferRequestDto, "AcceptedOfferRequestDto must not be null");

    Offer offer = offerRepository.findById(acceptedOfferRequestDto.offerId())
        .orElseThrow(() -> new OfferNotFoundException(acceptedOfferRequestDto.offerId()));
    Article article = offer.getArticle();

    if (!authenticatedUser.isOwner(Role.BUYER, article.getBuyer().getUser())) {
      throw new AccessDeniedException("Only the article's buyer can accept its offers");
    }
    if (!article.getId().equals(acceptedOfferRequestDto.articleId())) {
      throw new IllegalArgumentException("Offer with ID " + offer.getId() + " does not belong to article with ID "
          + acceptedOfferRequestDto.articleId());
    }
    if (acceptedOfferRepository.existsByArticleId(article.getId())) {
      throw new AcceptedOfferAlreadyExistException(article.getId());
    }
    if (article.getStatus() != StatusArticle.AVAILABLE) {
      throw new ArticleNotAvailableException(article.getId());
    }
    if (offer.getStatus() != StatusOffer.PENDING) {
      throw new OfferNotPendingException(offer.getId());
    }

    // Managed entities: the new statuses are flushed when the transaction commits
    offerRepository.findAllByArticleIdAndStatus(article.getId(), StatusOffer.PENDING)
        .forEach(pendingOffer -> pendingOffer.setStatus(StatusOffer.REJECTED));
    offer.setStatus(StatusOffer.ACCEPTED);
    article.setStatus(StatusArticle.BOUGHT);

    AcceptedOffer acceptedOffer = acceptedOfferRepository.save(new AcceptedOffer(offer));
    return acceptedOfferMapper.mapAcceptedOfferToResponseDto(acceptedOffer);
  }

  @Override
  public AcceptedOfferResponseDto getAcceptedOffer(Long id) {
    Objects.requireNonNull(id, "Accepted offer ID must not be null");
    AcceptedOffer acceptedOffer = acceptedOfferRepository.findById(id)
        .orElseThrow(() -> new AcceptedOfferNotFoundException(id));

    // The buyer who accepted the offer or the seller who made it
    if (!authenticatedUser.isOwner(Role.BUYER, acceptedOffer.getArticle().getBuyer().getUser())
        && !authenticatedUser.isOwner(Role.SELLER, acceptedOffer.getOffer().getSeller().getUser())) {
      throw new AccessDeniedException("Not allowed to view this accepted offer");
    }

    return acceptedOfferMapper.mapAcceptedOfferToResponseDto(acceptedOffer);
  }
}

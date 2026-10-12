package com.nicodev.iwanit.service;

import java.util.List;
import java.util.Objects;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.nicodev.iwanit.model.Article;
import com.nicodev.iwanit.model.Offer;
import com.nicodev.iwanit.model.Role;
import com.nicodev.iwanit.model.Seller;
import com.nicodev.iwanit.model.User;
import com.nicodev.iwanit.model.dto.OfferRequestDto;
import com.nicodev.iwanit.model.dto.OfferResponseDto;
import com.nicodev.iwanit.model.mapper.OfferMapper;
import com.nicodev.iwanit.repository.ArticleRepository;
import com.nicodev.iwanit.repository.OfferRepository;
import com.nicodev.iwanit.repository.SellerRepository;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class OfferServiceImpl implements OfferService {

  private final OfferRepository offerRepository;
  private final ArticleRepository articleRepository;
  private final SellerRepository sellerRepository;
  private final OfferMapper offerMapper;

  @Override
  @Transactional
  public OfferResponseDto createOffer(OfferRequestDto offerRequestDto) {
    Objects.requireNonNull(offerRequestDto, "OfferRequestDto must not be null");

    offerRepository.findByArticleIdAndSellerId(offerRequestDto.articleId(), offerRequestDto.sellerId())
        .ifPresent(existingOffer -> {
          // TODO: Consider creating an OfferAlreadyExists exception
          throw new IllegalArgumentException("An offer for this article and seller already exists.");
        });

    Article article = articleRepository.findById(offerRequestDto.articleId())
        .orElseThrow(
            () -> new IllegalArgumentException("Article with ID " + offerRequestDto.articleId() + " not found"));
    Seller seller = sellerRepository.findById(offerRequestDto.sellerId())
        .orElseThrow(() -> new IllegalArgumentException("Seller with ID " + offerRequestDto.sellerId() + " not found"));

    if (!isUser(currentUser(), Role.SELLER, seller.getUser())) {
      throw new AccessDeniedException("Offers can only be created as the authenticated seller");
    }

    try {
      Offer offer = offerMapper.mapRequestDtoToOffer(offerRequestDto, article, seller);
      offer = offerRepository.save(offer);
      return offerMapper.mapOfferToResponseDto(offer);
    } catch (Exception e) {
      // TODO: Consider creating an OfferNotCreatedException exception
      throw new RuntimeException("Failed to create offer: " + e.getMessage(), e);
    }
  }

  @Override
  public OfferResponseDto getOffer(Long id) {
    Objects.requireNonNull(id, "Offer ID must not be null");
    Offer offer = offerRepository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("Offer with ID " + id + " not found"));

    User caller = currentUser();
    if (!isUser(caller, Role.BUYER, offer.getArticle().getBuyer().getUser())
        && !isUser(caller, Role.SELLER, offer.getSeller().getUser())) {
      throw new AccessDeniedException("Not allowed to view this offer");
    }

    return offerMapper.mapOfferToResponseDto(offer);
  }

  @Override
  @Transactional
  public OfferResponseDto updateOffer(Long id, OfferRequestDto offerRequestDto) {
    Objects.requireNonNull(id, "Offer ID must not be null");
    Objects.requireNonNull(offerRequestDto, "OfferRequestDto must not be null");

    Offer offer = offerRepository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("Offer with ID " + id + " not found"));

    // Only the seller who made the offer can edit it, and it can't be moved to
    // another article or seller
    if (!isUser(currentUser(), Role.SELLER, offer.getSeller().getUser())) {
      throw new AccessDeniedException("Not allowed to modify this offer");
    }
    if (!offer.getArticle().getId().equals(offerRequestDto.articleId())
        || !offer.getSeller().getId().equals(offerRequestDto.sellerId())) {
      throw new IllegalArgumentException("The article and the seller of an offer can't be changed");
    }

    Article article = offer.getArticle();
    Seller seller = offer.getSeller();

    offer.setName(offerRequestDto.name());
    offer.setDescription(offerRequestDto.description());
    offer.setPrice(offerRequestDto.price().doubleValue());
    offer.setArticle(article);
    offer.setSeller(seller);

    Offer savedOffer = offerRepository.save(offer);
    return offerMapper.mapOfferToResponseDto(savedOffer);
  }

  @Override
  @Transactional
  public void deleteOffer(Long id) {
    Objects.requireNonNull(id, "Offer ID must not be null");
    Offer offer = offerRepository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("Offer with ID " + id + " does not exist"));

    // The buyer who owns the article or the seller who made the offer
    User caller = currentUser();
    if (!isUser(caller, Role.BUYER, offer.getArticle().getBuyer().getUser())
        && !isUser(caller, Role.SELLER, offer.getSeller().getUser())) {
      throw new AccessDeniedException("Not allowed to delete this offer");
    }

    offerRepository.deleteById(id);
  }

  @Override
  public List<OfferResponseDto> getAllSellerOffers(Long sellerId) {
    Objects.requireNonNull(sellerId, "Seller ID must not be null");

    Seller seller = sellerRepository.findById(sellerId)
        .orElseThrow(() -> new IllegalArgumentException("Seller with ID " + sellerId + " does not exist"));

    if (!isUser(currentUser(), Role.SELLER, seller.getUser())) {
      throw new AccessDeniedException("Sellers can only list their own offers");
    }

    return offerRepository.findAllBySellerId(sellerId).stream()
        .map(offerMapper::mapOfferToResponseDto)
        .toList();
  }

  /**
   * (non-Javadoc)
   * Finds all offers associated with a specific article
   * This service is called from the article view page to display all offers for a
   * given article
   * 
   * @see com.nicodev.iwanit.service.OfferService#getOffersByArticleId(java.lang.Long)
   */
  @Override
  public List<OfferResponseDto> getOffersByArticleId(Long articleId) {
    Objects.requireNonNull(articleId, "Article ID must not be null");

    Article article = articleRepository.findById(articleId)
        .orElseThrow(() -> new IllegalArgumentException("Article with ID " + articleId + " not found"));

    if (!isUser(currentUser(), Role.BUYER, article.getBuyer().getUser())) {
      throw new AccessDeniedException("Only the article's buyer can list its offers");
    }

    return offerRepository.findAllByArticleId(articleId).stream()
        .map(offerMapper::mapOfferToResponseDto)
        .toList();
  }

  private User currentUser() {
    var authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication == null || !(authentication.getPrincipal() instanceof User user)) {
      throw new AccessDeniedException("Authentication required");
    }
    return user;
  }

  private boolean isUser(User caller, Role role, User owner) {
    return caller.getRole() == role && owner != null && caller.getId().equals(owner.getId());
  }
}

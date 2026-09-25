package com.nicodev.iwanit.service;

import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Service;

import com.nicodev.iwanit.model.Article;
import com.nicodev.iwanit.model.Offer;
import com.nicodev.iwanit.model.Seller;
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
    return offerMapper.mapOfferToResponseDto(offer);
  }

  @Override
  @Transactional
  public OfferResponseDto updateOffer(Long id, OfferRequestDto offerRequestDto) {
    Objects.requireNonNull(id, "Offer ID must not be null");
    Objects.requireNonNull(offerRequestDto, "OfferRequestDto must not be null");

    Offer offer = offerRepository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("Offer with ID " + id + " not found"));

    Article article = articleRepository.findById(offerRequestDto.articleId())
        .orElseThrow(
            () -> new IllegalArgumentException("Article with ID " + offerRequestDto.articleId() + " not found"));
    Seller seller = sellerRepository.findById(offerRequestDto.sellerId())
        .orElseThrow(() -> new IllegalArgumentException("Seller with ID " + offerRequestDto.sellerId() + " not found"));

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
    if (!offerRepository.existsById(id)) {
      throw new IllegalArgumentException("Offer with ID " + id + " does not exist");
    }

    offerRepository.deleteById(id);
  }

  @Override
  public List<OfferResponseDto> getAllSellerOffers(Long sellerId) {
    Objects.requireNonNull(sellerId, "Seller ID must not be null");

    if (!sellerRepository.existsById(sellerId)) {
      throw new IllegalArgumentException("Seller with ID " + sellerId + " does not exist");
    }

    return offerRepository.findAllBySellerId(sellerId).stream()
        .map(offerMapper::mapOfferToResponseDto)
        .toList();
  }
}

package com.nicodev.iwanit.model.mapper;

import org.springframework.stereotype.Component;

import com.nicodev.iwanit.model.Article;
import com.nicodev.iwanit.model.Offer;
import com.nicodev.iwanit.model.Seller;
import com.nicodev.iwanit.model.dto.OfferRequestDto;
import com.nicodev.iwanit.model.dto.OfferResponseDto;

@Component
public class OfferMapper {

  public OfferResponseDto mapOfferToResponseDto(Offer offer) {
    return new OfferResponseDto(offer.getId(), offer.getName(), offer.getDescription(), offer.getPrice(),
        offer.getArticle().getId(), offer.getSeller().getId());
  }

  public Offer mapRequestDtoToOffer(OfferRequestDto offerRequestDto, Article article, Seller seller) {
    Offer offer = new Offer();
    offer.setName(offerRequestDto.name());
    offer.setDescription(offerRequestDto.description());
    offer.setPrice(offerRequestDto.price().doubleValue());
    offer.setArticle(article);
    offer.setSeller(seller);
    return offer;
  }
}

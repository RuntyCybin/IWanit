package com.nicodev.iwanit.model.mapper;

import org.springframework.stereotype.Component;

import com.nicodev.iwanit.model.AcceptedOffer;
import com.nicodev.iwanit.model.dto.AcceptedOfferResponseDto;

@Component
public class AcceptedOfferMapper {

  public AcceptedOfferResponseDto mapAcceptedOfferToResponseDto(AcceptedOffer acceptedOffer) {
    return new AcceptedOfferResponseDto(acceptedOffer.getId(), acceptedOffer.getOffer().getId(),
        acceptedOffer.getArticle().getId(), acceptedOffer.getOffer().getSeller().getId(),
        acceptedOffer.getOffer().getPrice(), acceptedOffer.getAcceptedAt());
  }
}

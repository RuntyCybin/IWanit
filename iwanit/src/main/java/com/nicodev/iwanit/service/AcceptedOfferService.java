package com.nicodev.iwanit.service;

import com.nicodev.iwanit.model.dto.AcceptedOfferRequestDto;
import com.nicodev.iwanit.model.dto.AcceptedOfferResponseDto;

public interface AcceptedOfferService {
  AcceptedOfferResponseDto createAcceptedOffer(AcceptedOfferRequestDto acceptedOfferRequestDto);

  AcceptedOfferResponseDto getAcceptedOffer(Long id);
}

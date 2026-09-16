package com.nicodev.iwanit.service;

import java.util.List;

import com.nicodev.iwanit.model.dto.OfferRequestDto;
import com.nicodev.iwanit.model.dto.OfferResponseDto;

public interface OfferService {
  OfferResponseDto createOffer(OfferRequestDto offerRequestDto);

  OfferResponseDto getOffer(Long id);

  List<OfferResponseDto> getAllSellerOffers(Long sellerId);

  OfferResponseDto updateOffer(Long id, OfferRequestDto offerRequestDto);

  void deleteOffer(Long id);
}

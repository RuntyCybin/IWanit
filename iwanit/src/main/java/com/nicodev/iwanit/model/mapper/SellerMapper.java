package com.nicodev.iwanit.model.mapper;

import java.util.List;

import org.springframework.stereotype.Component;

import com.nicodev.iwanit.model.Seller;
import com.nicodev.iwanit.model.User;
import com.nicodev.iwanit.model.dto.OfferResponseDto;
import com.nicodev.iwanit.model.dto.SellerRequestDto;
import com.nicodev.iwanit.model.dto.SellerResponseDto;

import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class SellerMapper {

  private final OfferMapper offerMapper;

  public Seller mapSellerRequestToSeller(SellerRequestDto requestDto, User user) {
    return new Seller(requestDto.name(), requestDto.email(), requestDto.phoneNumber(), user);
  }

  public SellerResponseDto mapSellerToResponse(Seller seller) {
    List<OfferResponseDto> offers = seller.getOffers() == null
        ? List.of()
        : seller.getOffers().stream().map(offerMapper::mapOfferToResponseDto).toList();

    return new SellerResponseDto(
        seller.getId(),
        seller.getName(),
        seller.getEmail(),
        seller.getPhoneNumber(),
        seller.getUser().getId(),
        offers);
  }
}

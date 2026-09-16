package com.nicodev.iwanit.model.mapper;

import org.springframework.stereotype.Component;

import com.nicodev.iwanit.model.Buyer;
import com.nicodev.iwanit.model.User;
import com.nicodev.iwanit.model.dto.BuyerRequestDto;
import com.nicodev.iwanit.model.dto.BuyerResponseDto;

@Component
public class BuyerMapper {

  public Buyer mapBuyerRequestDtoToBuyer(BuyerRequestDto buyerRequestDto, User user) {
    return new Buyer(
        buyerRequestDto.name(),
        buyerRequestDto.email(),
        buyerRequestDto.phoneNumber(),
        user);
  }

  public BuyerResponseDto mapBuyerToResponseDto(Buyer buyer) {
    return new BuyerResponseDto(
        buyer.getId(),
        buyer.getName(),
        buyer.getEmail(),
        buyer.getPhoneNumber());
  }

}

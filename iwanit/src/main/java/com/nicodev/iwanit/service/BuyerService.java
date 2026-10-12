package com.nicodev.iwanit.service;

import java.util.List;

import com.nicodev.iwanit.model.User;
import com.nicodev.iwanit.model.dto.BuyerRequestDto;
import com.nicodev.iwanit.model.dto.BuyerResponseDto;

public interface BuyerService {

  BuyerResponseDto createBuyer(Long userId, BuyerRequestDto buyerRequestDto);

  List<BuyerResponseDto> getAllBuyers(User caller);

  BuyerResponseDto getBuyerById(Long id, Long userId);

  void deleteBuyer(Long id, Long userId);

  BuyerResponseDto getBuyerByUserId(Long userId, Long authenticatedUserId);

  BuyerResponseDto updateBuyer(Long userId, BuyerRequestDto buyerRequestDto);
}
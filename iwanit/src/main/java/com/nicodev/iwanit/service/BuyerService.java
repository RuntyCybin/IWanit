package com.nicodev.iwanit.service;

import java.util.List;

import com.nicodev.iwanit.model.dto.BuyerRequestDto;
import com.nicodev.iwanit.model.dto.BuyerResponseDto;

public interface BuyerService {

  BuyerResponseDto createBuyer(BuyerRequestDto buyerRequestDto);

  List<BuyerResponseDto> getAllBuyers();

  BuyerResponseDto getBuyerById(Long id);

  void deleteBuyer(Long id);
}
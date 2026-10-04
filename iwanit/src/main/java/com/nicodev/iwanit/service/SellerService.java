package com.nicodev.iwanit.service;

import java.util.List;

import com.nicodev.iwanit.model.dto.SellerRequestDto;
import com.nicodev.iwanit.model.dto.SellerResponseDto;

public interface SellerService {

  SellerResponseDto createSeller(Long userId, SellerRequestDto sellerRequestDto);

  SellerResponseDto getSellerById(Long id);

  List<SellerResponseDto> getAllSellers();

  void deleteSeller(Long id, Long userId);

  SellerResponseDto getSellerByUserId(Long userId);

  SellerResponseDto updateSeller(Long userId, SellerRequestDto sellerRequestDto);
}

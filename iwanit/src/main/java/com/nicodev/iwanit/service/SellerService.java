package com.nicodev.iwanit.service;

import com.nicodev.iwanit.model.dto.SellerRequestDto;
import com.nicodev.iwanit.model.dto.SellerResponseDto;

public interface SellerService {

  SellerResponseDto createSeller(SellerRequestDto sellerRequestDto);

  SellerResponseDto getSellerById(Long id);

  void deleteSeller(Long id);
}

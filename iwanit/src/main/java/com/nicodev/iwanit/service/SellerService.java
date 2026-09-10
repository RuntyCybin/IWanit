package com.nicodev.iwanit.service;

import com.nicodev.iwanit.model.dto.SellerResponseDto;

public interface SellerService {

  SellerResponseDto createSeller(String name, String email, String phoneNumber);

  SellerResponseDto getSellerById(Long id);

  void deleteSeller(Long id);
}

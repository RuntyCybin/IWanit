package com.nicodev.iwanit.service;

import java.util.Objects;

import org.springframework.stereotype.Service;

import com.nicodev.iwanit.model.Seller;
import com.nicodev.iwanit.model.dto.SellerRequestDto;
import com.nicodev.iwanit.model.dto.SellerResponseDto;
import com.nicodev.iwanit.repository.SellerRepository;
import com.nicodev.iwanit.repository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class SellerServiceImpl implements SellerService {

  private final SellerRepository sellerRepository;
  private final UserRepository userRepository;

  @Override
  @Transactional
  public SellerResponseDto createSeller(SellerRequestDto sellerRequestDto) {
    Objects.requireNonNull(sellerRequestDto, "SellerRequestDto must not be null");

    var user = userRepository.findByEmail(sellerRequestDto.email())
        .orElseThrow(() -> new RuntimeException("User not found with email: " + sellerRequestDto.email()));

    var seller = new Seller(
        sellerRequestDto.name(),
        sellerRequestDto.email(),
        sellerRequestDto.phoneNumber(),
        user);

    try {
      var savedSeller = sellerRepository.save(seller);
      return new SellerResponseDto(
          savedSeller.getId(),
          savedSeller.getName(),
          savedSeller.getEmail(),
          savedSeller.getPhoneNumber());
    } catch (Exception e) {
      throw new RuntimeException("Failed to create seller", e);
    }
  }

  @Override
  public SellerResponseDto getSellerById(Long id) {
    Objects.requireNonNull(id, "Seller ID must not be null");

    return sellerRepository.findById(id)
        .map(seller -> new SellerResponseDto(
            seller.getId(),
            seller.getName(),
            seller.getEmail(),
            seller.getPhoneNumber()))
        .orElseThrow(() -> new RuntimeException("Seller not found with ID: " + id));
  }

  @Override
  @Transactional
  public void deleteSeller(Long id) {
    Objects.requireNonNull(id, "Seller ID must not be null");

    sellerRepository.findById(id).ifPresentOrElse(
        seller -> {
          try {
            sellerRepository.delete(seller);
          } catch (Exception e) {
            throw new RuntimeException("Failed to delete seller with ID: " + id, e);
          }
        },
        () -> {
          throw new RuntimeException("Seller not found with ID: " + id);
        });

  }

}

package com.nicodev.iwanit.service;

import java.util.List;
import java.util.Objects;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import com.nicodev.iwanit.model.dto.SellerRequestDto;
import com.nicodev.iwanit.model.dto.SellerResponseDto;
import com.nicodev.iwanit.model.mapper.SellerMapper;
import com.nicodev.iwanit.repository.SellerRepository;
import com.nicodev.iwanit.repository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class SellerServiceImpl implements SellerService {

  private final SellerRepository sellerRepository;
  private final UserRepository userRepository;
  private final SellerMapper sellerMapper;

  @Override
  @Transactional
  public SellerResponseDto createSeller(Long userId, SellerRequestDto sellerRequestDto) {
    Objects.requireNonNull(userId, "User ID must not be null");
    Objects.requireNonNull(sellerRequestDto, "SellerRequestDto must not be null");

    var user = userRepository.findById(userId)
        .orElseThrow(() -> new RuntimeException("User not found with email: " + sellerRequestDto.email()));

    var seller = this.sellerMapper.mapSellerRequestToSeller(sellerRequestDto, user);

    try {
      var savedSeller = sellerRepository.save(seller);
      return this.sellerMapper.mapSellerToResponse(savedSeller);
    } catch (Exception e) {
      throw new RuntimeException("Failed to create seller", e);
    }
  }

  @Override
  public SellerResponseDto getSellerById(Long id) {
    Objects.requireNonNull(id, "Seller ID must not be null");

    return sellerRepository.findById(id)
        .map(this.sellerMapper::mapSellerToResponse)
        .orElseThrow(() -> new RuntimeException("Seller not found with ID: " + id));
  }

  @Override
  public List<SellerResponseDto> getAllSellers() {
    return sellerRepository.findAll().stream()
        .map(this.sellerMapper::mapSellerToResponse)
        .toList();
  }

  @Override
  @Transactional
  public void deleteSeller(Long id, Long userId) {
    Objects.requireNonNull(id, "Seller ID must not be null");
    Objects.requireNonNull(userId, "User ID must not be null");

    sellerRepository.findById(id).ifPresentOrElse(
        seller -> {
          if (!seller.getUser().getId().equals(userId)) {
            throw new AccessDeniedException("Seller " + id + " does not belong to the authenticated user");
          }
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

  @Override
  public SellerResponseDto getSellerByUserId(Long userId) {
    Objects.requireNonNull(userId, "User ID must not be null");

    return sellerRepository.findByUserId(userId)
        .map(this.sellerMapper::mapSellerToResponse)
        .orElseThrow(() -> new RuntimeException("Seller not found for user ID: " + userId));
  }

  @Override
  @Transactional
  public SellerResponseDto updateSeller(Long userId, SellerRequestDto sellerRequestDto) {
    Objects.requireNonNull(userId, "User ID must not be null");
    Objects.requireNonNull(sellerRequestDto, "SellerRequestDto must not be null");

    var updatedSeller = sellerRepository.findByUserId(userId)
        .orElseThrow(() -> new RuntimeException("Seller not found for user ID: " + userId));

    updatedSeller.setName(sellerRequestDto.name());
    updatedSeller.setEmail(sellerRequestDto.email());
    updatedSeller.setPhoneNumber(sellerRequestDto.phoneNumber());

    try {
      var savedSeller = sellerRepository.save(updatedSeller);
      return this.sellerMapper.mapSellerToResponse(savedSeller);
    } catch (Exception e) {
      throw new RuntimeException("Failed to update seller for user ID: " + userId, e);
    }
  }

}

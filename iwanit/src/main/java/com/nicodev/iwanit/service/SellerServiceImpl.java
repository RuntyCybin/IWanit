package com.nicodev.iwanit.service;

import java.util.List;
import java.util.Objects;

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
  public SellerResponseDto createSeller(SellerRequestDto sellerRequestDto) {
    Objects.requireNonNull(sellerRequestDto, "SellerRequestDto must not be null");

    var user = userRepository.findById(sellerRequestDto.userId())
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

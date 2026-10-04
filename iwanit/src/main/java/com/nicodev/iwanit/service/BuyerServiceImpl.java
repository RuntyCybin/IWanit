package com.nicodev.iwanit.service;

import java.util.List;
import java.util.Objects;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import com.nicodev.iwanit.exception.BuyerNotDeletedException;
import com.nicodev.iwanit.exception.BuyerNotFoundException;
import com.nicodev.iwanit.exception.BuyerNotSavedException;
import com.nicodev.iwanit.exception.UserNotFoundException;
import com.nicodev.iwanit.model.dto.BuyerRequestDto;
import com.nicodev.iwanit.model.dto.BuyerResponseDto;
import com.nicodev.iwanit.model.mapper.BuyerMapper;
import com.nicodev.iwanit.repository.BuyerRepository;
import com.nicodev.iwanit.repository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class BuyerServiceImpl implements BuyerService {

  private final UserRepository userRepository;
  private final BuyerRepository buyerRepository;
  private final BuyerMapper buyerMapper;

  @Override
  @Transactional
  public BuyerResponseDto createBuyer(Long userId, BuyerRequestDto buyerRequestDto) {

    Objects.requireNonNull(userId, "User ID must not be null");
    Objects.requireNonNull(buyerRequestDto);

    var user = userRepository.findById(userId)
        .orElseThrow(() -> new UserNotFoundException(buyerRequestDto.email()));

    var buyer = buyerMapper.mapBuyerRequestDtoToBuyer(buyerRequestDto, user);

    try {
      buyerRepository.save(buyer);

      return buyerMapper.mapBuyerToResponseDto(buyer);
    } catch (Exception e) {
      throw new BuyerNotSavedException(buyerRequestDto.email(), e.getMessage());
    }
  }

  @Override
  public List<BuyerResponseDto> getAllBuyers() {
    return buyerRepository.findAll().stream()
        .map(buyerMapper::mapBuyerToResponseDto)
        .toList();
  }

  @Override
  public BuyerResponseDto getBuyerById(Long id) {
    Objects.requireNonNull(id, "Buyer ID must not be null");

    var buyer = buyerRepository.findById(id)
        .orElseThrow(() -> new BuyerNotFoundException(id, "Buyer not found"));

    return buyerMapper.mapBuyerToResponseDto(buyer);
  }

  @Override
  @Transactional
  public void deleteBuyer(Long id, Long userId) {
    Objects.requireNonNull(id, "Buyer ID must not be null");
    Objects.requireNonNull(userId, "User ID must not be null");

    var buyer = buyerRepository.findById(id)
        .orElseThrow(() -> new BuyerNotFoundException(id, "Buyer not found"));

    if (!buyer.getUser().getId().equals(userId)) {
      throw new AccessDeniedException("Buyer " + id + " does not belong to the authenticated user");
    }

    try {
      buyerRepository.delete(buyer);
    } catch (Exception e) {
      throw new BuyerNotDeletedException(id, e.getMessage());
    }
  }

  @Override
  public BuyerResponseDto getBuyerByUserId(Long userId) {
    Objects.requireNonNull(userId, "User ID must not be null");

    var buyer = buyerRepository.findByUserId(userId)
        .orElseThrow(() -> new BuyerNotFoundException(userId, "Buyer not found for user ID"));

    return buyerMapper.mapBuyerToResponseDto(buyer);
  }

  @Override
  @Transactional
  public BuyerResponseDto updateBuyer(Long userId, BuyerRequestDto buyerRequestDto) {
    Objects.requireNonNull(userId, "User ID must not be null");
    Objects.requireNonNull(buyerRequestDto, "BuyerRequestDto must not be null");

    var updatedBuyer = buyerRepository.findByUserId(userId)
        .orElseThrow(() -> new BuyerNotFoundException(userId, "Buyer not found for user ID"));

    updatedBuyer.setName(buyerRequestDto.name());
    updatedBuyer.setEmail(buyerRequestDto.email());
    updatedBuyer.setPhoneNumber(buyerRequestDto.phoneNumber());

    try {
      buyerRepository.save(updatedBuyer);
      return buyerMapper.mapBuyerToResponseDto(updatedBuyer);
    } catch (Exception e) {
      throw new BuyerNotSavedException(buyerRequestDto.email(), e.getMessage());
    }
  }
}

package com.nicodev.iwanit.service;

import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Service;

import com.nicodev.iwanit.exception.BuyerNotDeletedException;
import com.nicodev.iwanit.exception.BuyerNotFoundException;
import com.nicodev.iwanit.exception.BuyerNotSavedException;
import com.nicodev.iwanit.exception.UserNotFoundByEmailException;
import com.nicodev.iwanit.model.Buyer;
import com.nicodev.iwanit.model.dto.BuyerRequestDto;
import com.nicodev.iwanit.model.dto.BuyerResponseDto;
import com.nicodev.iwanit.repository.BuyerRepository;
import com.nicodev.iwanit.repository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class BuyerServiceImpl implements BuyerService {

  private final UserRepository userRepository;
  private final BuyerRepository buyerRepository;

  @Override
  @Transactional
  public BuyerResponseDto createBuyer(BuyerRequestDto buyerRequestDto) {

    Objects.requireNonNull(buyerRequestDto);

    var user = userRepository.findByEmail(buyerRequestDto.email())
        .orElseThrow(() -> new UserNotFoundByEmailException(buyerRequestDto.email()));

    var buyer = new Buyer(
        buyerRequestDto.name(),
        buyerRequestDto.email(),
        buyerRequestDto.phoneNumber(),
        user);

    try {
      buyerRepository.save(buyer);
    } catch (Exception e) {
      throw new BuyerNotSavedException(buyerRequestDto.email(), e.getMessage());
    }

    return new BuyerResponseDto(
        buyer.getId(),
        buyer.getName(),
        buyer.getEmail(),
        buyer.getPhoneNumber());
  }

  @Override
  public List<BuyerResponseDto> getAllBuyers() {
    return buyerRepository.findAll().stream()
        .map(buyer -> new BuyerResponseDto(
            buyer.getId(),
            buyer.getName(),
            buyer.getEmail(),
            buyer.getPhoneNumber()))
        .toList();
  }

  @Override
  public BuyerResponseDto getBuyerById(Long id) {
    Objects.requireNonNull(id, "Buyer ID must not be null");

    var buyer = buyerRepository.findById(id)
        .orElseThrow(() -> new BuyerNotFoundException(id, "Buyer not found"));

    return new BuyerResponseDto(
        buyer.getId(),
        buyer.getName(),
        buyer.getEmail(),
        buyer.getPhoneNumber());
  }

  @Override
  @Transactional
  public void deleteBuyer(Long id) {
    Objects.requireNonNull(id, "Buyer ID must not be null");

    var buyer = buyerRepository.findById(id)
        .orElseThrow(() -> new BuyerNotFoundException(id, "Buyer not found"));

    try {
      buyerRepository.delete(buyer);
    } catch (Exception e) {
      throw new BuyerNotDeletedException(id, e.getMessage());
    }
  }
}

package com.nicodev.iwanit.service;

import java.util.List;
import java.util.Objects;
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
  public BuyerResponseDto createBuyer(BuyerRequestDto buyerRequestDto) {

    Objects.requireNonNull(buyerRequestDto);

    var user = userRepository.findById(buyerRequestDto.userId())
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

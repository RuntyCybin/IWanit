package com.nicodev.iwanit.service;

import java.util.List;
import java.util.Objects;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import com.nicodev.iwanit.exception.BuyerNotDeletedException;
import com.nicodev.iwanit.exception.BuyerNotFoundException;
import com.nicodev.iwanit.exception.BuyerNotSavedException;
import com.nicodev.iwanit.exception.UserNotFoundException;
import com.nicodev.iwanit.model.Buyer;
import com.nicodev.iwanit.model.Role;
import com.nicodev.iwanit.model.dto.BuyerRequestDto;
import com.nicodev.iwanit.model.dto.BuyerResponseDto;
import com.nicodev.iwanit.model.mapper.BuyerMapper;
import com.nicodev.iwanit.repository.BuyerRepository;
import com.nicodev.iwanit.repository.UserRepository;
import com.nicodev.iwanit.security.AuthenticatedUser;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class BuyerServiceImpl implements BuyerService {

  private final UserRepository userRepository;
  private final BuyerRepository buyerRepository;
  private final BuyerMapper buyerMapper;
  private final AuthenticatedUser authenticatedUser;

  /**
   * Creates the buyer of the authenticated user, who must have the BUYER role.
   */
  @Override
  @Transactional
  public BuyerResponseDto createBuyer(BuyerRequestDto buyerRequestDto) {
    Objects.requireNonNull(buyerRequestDto, "BuyerRequestDto must not be null");

    if (!authenticatedUser.hasRole(Role.BUYER)) {
      throw new AccessDeniedException("Only users with the BUYER role can create a buyer");
    }

    Long userId = authenticatedUser.get().getId();
    var user = userRepository.findById(userId)
        .orElseThrow(() -> new UserNotFoundException(userId.toString()));

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
    if (!authenticatedUser.hasRole(Role.ADMIN)) {
      throw new AccessDeniedException("Only admins can list all the buyers");
    }

    return buyerRepository.findAll().stream()
        .map(buyerMapper::mapBuyerToResponseDto)
        .toList();
  }

  @Override
  public BuyerResponseDto getBuyerById(Long id) {
    Objects.requireNonNull(id, "Buyer ID must not be null");

    var buyer = buyerRepository.findById(id)
        .orElseThrow(() -> new BuyerNotFoundException(id));

    if (!authenticatedUser.isOwner(Role.BUYER, buyer.getUser())) {
      throw new AccessDeniedException("Buyer " + id + " does not belong to the authenticated user");
    }

    return buyerMapper.mapBuyerToResponseDto(buyer);
  }

  @Override
  @Transactional
  public void deleteBuyer(Long id) {
    Objects.requireNonNull(id, "Buyer ID must not be null");

    var buyer = buyerRepository.findById(id)
        .orElseThrow(() -> new BuyerNotFoundException(id));

    if (!authenticatedUser.isOwner(Role.BUYER, buyer.getUser())) {
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
        .orElseThrow(() -> new BuyerNotFoundException(userId));

    if (!authenticatedUser.isOwner(Role.BUYER, buyer.getUser())) {
      throw new AccessDeniedException("Buyer data can only be read by its own user");
    }

    return buyerMapper.mapBuyerToResponseDto(buyer);
  }

  /**
   * Updates the buyer of the authenticated user. The target user is taken from
   * the authenticated user, never from the request body.
   */
  @Override
  @Transactional
  public BuyerResponseDto updateBuyer(BuyerRequestDto buyerRequestDto) {
    Objects.requireNonNull(buyerRequestDto, "BuyerRequestDto must not be null");

    Long userId = authenticatedUser.get().getId();
    Buyer updatedBuyer = buyerRepository.findByUserId(userId)
        .orElseThrow(() -> new BuyerNotFoundException(userId));

    if (!authenticatedUser.isOwner(Role.BUYER, updatedBuyer.getUser())) {
      throw new AccessDeniedException("Only users with the BUYER role can update a buyer");
    }

    updatedBuyer.setName(buyerRequestDto.name());
    updatedBuyer.setEmail(buyerRequestDto.email());
    updatedBuyer.setPhoneNumber(buyerRequestDto.phoneNumber());
    updatedBuyer.setAddress(buyerRequestDto.address());
    updatedBuyer.setCountry(buyerRequestDto.country());
    updatedBuyer.setCp(buyerRequestDto.cp());

    try {
      buyerRepository.save(updatedBuyer);
      return buyerMapper.mapBuyerToResponseDto(updatedBuyer);
    } catch (Exception e) {
      throw new BuyerNotSavedException(buyerRequestDto.email(), e.getMessage());
    }
  }
}

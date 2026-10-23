package com.nicodev.iwanit.service;

import java.util.List;
import java.util.Objects;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import com.nicodev.iwanit.exception.SellerNotFoundException;
import com.nicodev.iwanit.exception.UserNotFoundException;
import com.nicodev.iwanit.model.Role;
import com.nicodev.iwanit.model.Seller;
import com.nicodev.iwanit.model.dto.SellerRequestDto;
import com.nicodev.iwanit.model.dto.SellerResponseDto;
import com.nicodev.iwanit.model.mapper.SellerMapper;
import com.nicodev.iwanit.repository.SellerRepository;
import com.nicodev.iwanit.repository.UserRepository;
import com.nicodev.iwanit.security.AuthenticatedUser;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class SellerServiceImpl implements SellerService {

  private final SellerRepository sellerRepository;
  private final UserRepository userRepository;
  private final SellerMapper sellerMapper;
  private final AuthenticatedUser authenticatedUser;

  /**
   * Creates the seller of the authenticated user, who must have the SELLER role.
   */
  @Override
  @Transactional
  public SellerResponseDto createSeller(SellerRequestDto sellerRequestDto) {
    Objects.requireNonNull(sellerRequestDto, "SellerRequestDto must not be null");

    if (!authenticatedUser.hasRole(Role.SELLER)) {
      throw new AccessDeniedException("Only users with the SELLER role can create a seller");
    }

    Long userId = authenticatedUser.get().getId();
    var user = userRepository.findById(userId)
        .orElseThrow(() -> new UserNotFoundException(userId.toString()));

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

    Seller seller = sellerRepository.findById(id)
        .orElseThrow(() -> new SellerNotFoundException(id));

    if (!authenticatedUser.isOwner(Role.SELLER, seller.getUser())) {
      throw new AccessDeniedException("Seller " + id + " does not belong to the authenticated user");
    }

    return this.sellerMapper.mapSellerToResponse(seller);
  }

  @Override
  public List<SellerResponseDto> getAllSellers() {
    if (!authenticatedUser.hasRole(Role.ADMIN)) {
      throw new AccessDeniedException("Only admins can list all the sellers");
    }

    return sellerRepository.findAll().stream()
        .map(this.sellerMapper::mapSellerToResponse)
        .toList();
  }

  @Override
  @Transactional
  public void deleteSeller(Long id) {
    Objects.requireNonNull(id, "Seller ID must not be null");

    Seller seller = sellerRepository.findById(id)
        .orElseThrow(() -> new SellerNotFoundException(id));

    if (!authenticatedUser.isOwner(Role.SELLER, seller.getUser())) {
      throw new AccessDeniedException("Seller " + id + " does not belong to the authenticated user");
    }

    try {
      sellerRepository.delete(seller);
    } catch (Exception e) {
      throw new RuntimeException("Failed to delete seller with ID: " + id, e);
    }
  }

  @Override
  public SellerResponseDto getSellerByUserId(Long userId) {
    Objects.requireNonNull(userId, "User ID must not be null");

    Seller seller = sellerRepository.findByUserId(userId)
        .orElseThrow(() -> new SellerNotFoundException(userId));

    if (!authenticatedUser.isOwner(Role.SELLER, seller.getUser())) {
      throw new AccessDeniedException("Seller data can only be read by its own user");
    }

    return this.sellerMapper.mapSellerToResponse(seller);
  }

  /**
   * Updates the seller of the authenticated user. The target user is taken from
   * the authenticated user, never from the request body.
   */
  @Override
  @Transactional
  public SellerResponseDto updateSeller(SellerRequestDto sellerRequestDto) {
    Objects.requireNonNull(sellerRequestDto, "SellerRequestDto must not be null");

    Long userId = authenticatedUser.get().getId();
    Seller updatedSeller = sellerRepository.findByUserId(userId)
        .orElseThrow(() -> new SellerNotFoundException(userId));

    if (!authenticatedUser.isOwner(Role.SELLER, updatedSeller.getUser())) {
      throw new AccessDeniedException("Only users with the SELLER role can update a seller");
    }

    updatedSeller.setName(sellerRequestDto.name());
    updatedSeller.setEmail(sellerRequestDto.email());
    updatedSeller.setPhoneNumber(sellerRequestDto.phoneNumber());
    updatedSeller.setIBAN(sellerRequestDto.IBAN());
    updatedSeller.setPayPal(sellerRequestDto.PayPal());

    try {
      var savedSeller = sellerRepository.save(updatedSeller);
      return this.sellerMapper.mapSellerToResponse(savedSeller);
    } catch (Exception e) {
      throw new RuntimeException("Failed to update seller for user ID: " + userId, e);
    }
  }

}

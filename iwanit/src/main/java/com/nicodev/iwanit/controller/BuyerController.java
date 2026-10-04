package com.nicodev.iwanit.controller;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nicodev.iwanit.model.User;
import com.nicodev.iwanit.model.dto.BuyerRequestDto;
import com.nicodev.iwanit.model.dto.BuyerResponseDto;
import com.nicodev.iwanit.service.BuyerService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/v1/buyers")
@AllArgsConstructor
public class BuyerController {

  private final BuyerService buyerService;

  /**
   * Creates a buyer related to a user
   *
   * @param buyerRequestDto the buyer request DTO
   * @return the created buyer response DTO
   */
  @PostMapping
  public ResponseEntity<BuyerResponseDto> createBuyer(
      @AuthenticationPrincipal User user,
      @RequestBody BuyerRequestDto buyerRequestDto) {
    BuyerResponseDto createdBuyer = this.buyerService.createBuyer(user.getId(), buyerRequestDto);
    return ResponseEntity
        .created(URI.create("/v1/buyers/" + createdBuyer.id()))
        .body(createdBuyer);
  }

  /**
   * Gets a buyer by its ID
   * 
   * @param id
   * @return
   */
  @GetMapping("/{id}")
  public ResponseEntity<BuyerResponseDto> getBuyer(@PathVariable Long id) {
    return ResponseEntity.ok(this.buyerService.getBuyerById(id));
  }

  /**
   * Gets all the buyers
   * 
   * @return
   */
  @GetMapping
  public ResponseEntity<List<BuyerResponseDto>> getAllBuyers() {
    return ResponseEntity.ok(this.buyerService.getAllBuyers());
  }

  /**
   * Deletes a buyer
   * 
   * @param id
   * @return
   */
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteBuyer(@AuthenticationPrincipal User user, @PathVariable Long id) {
    this.buyerService.deleteBuyer(id, user.getId());
    return ResponseEntity.noContent().build();
  }

  /**
   * Get a buyer by user ID
   */
  @GetMapping("/user/{userId}")
  public ResponseEntity<BuyerResponseDto> getBuyerByUserId(@PathVariable Long userId) {
    return ResponseEntity.ok(this.buyerService.getBuyerByUserId(userId));
  }

  /**
   * Update the buyer of the authenticated user. The target user is taken from
   * the JWT principal, never from the request body.
   */
  @PutMapping
  public ResponseEntity<BuyerResponseDto> updateBuyer(
      @AuthenticationPrincipal User user,
      @RequestBody BuyerRequestDto buyerRequestDto) {
    BuyerResponseDto updatedBuyer = this.buyerService.updateBuyer(user.getId(), buyerRequestDto);
    return ResponseEntity.ok(updatedBuyer);
  }
}

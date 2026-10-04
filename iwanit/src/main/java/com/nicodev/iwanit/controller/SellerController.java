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
import com.nicodev.iwanit.model.dto.SellerRequestDto;
import com.nicodev.iwanit.model.dto.SellerResponseDto;
import com.nicodev.iwanit.service.SellerService;

import lombok.AllArgsConstructor;

/**
 * REST controller exposing the seller endpoints under {@code /v1/sellers}.
 *
 * A seller is always linked to an existing user through a one-to-one relation,
 * and is the role that answers an article with an offer. Sellers are not
 * updated through this controller: the underlying user is updated instead.
 *
 * @see SellerService
 */
@RestController
@RequestMapping("/v1/sellers")
@AllArgsConstructor
public class SellerController {

  private final SellerService sellerService;

  /**
   * Creates a seller related to the authenticated user.
   *
   * @param user             the authenticated user the seller belongs to
   * @param sellerRequestDto the seller data to persist
   * @return {@code 201 Created} with the persisted seller and a {@code Location}
   *         header pointing to the new resource
   */
  @PostMapping
  public ResponseEntity<SellerResponseDto> createSeller(
      @AuthenticationPrincipal User user,
      @RequestBody SellerRequestDto sellerRequestDto) {
    var createdSeller = this.sellerService.createSeller(user.getId(), sellerRequestDto);
    return ResponseEntity
        .created(URI.create("/v1/sellers/" + createdSeller.id()))
        .body(createdSeller);
  }

  /**
   * Gets a single seller by its ID.
   *
   * @param id the ID of the seller to retrieve
   * @return {@code 200 OK} with the matching seller and its offers
   */
  @GetMapping("/{id}")
  public ResponseEntity<SellerResponseDto> getSeller(@PathVariable Long id) {
    return ResponseEntity.ok(this.sellerService.getSellerById(id));
  }

  /**
   * Gets every seller registered in the system.
   *
   * @return {@code 200 OK} with the list of sellers, empty if there is none
   */
  @GetMapping
  public ResponseEntity<List<SellerResponseDto>> getAllSellers() {
    return ResponseEntity.ok(this.sellerService.getAllSellers());
  }

  /**
   * Deletes a seller by its ID.
   *
   * @param id the ID of the seller to delete
   * @return {@code 204 No Content} once the seller has been deleted
   */
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteSeller(@AuthenticationPrincipal User user, @PathVariable Long id) {
    this.sellerService.deleteSeller(id, user.getId());
    return ResponseEntity.noContent().build();
  }

  /**
   * Get a seller by user ID
   */
  @GetMapping("/user/{userId}")
  public ResponseEntity<SellerResponseDto> getSellerByUserId(@PathVariable Long userId) {
    return ResponseEntity.ok(this.sellerService.getSellerByUserId(userId));
  }

  /**
   * Update the seller of the authenticated user. The target user is taken from
   * the JWT principal, never from the request body.
   */
  @PutMapping
  public ResponseEntity<SellerResponseDto> updateSeller(
      @AuthenticationPrincipal User user,
      @RequestBody SellerRequestDto sellerRequestDto) {
    SellerResponseDto updatedSeller = this.sellerService.updateSeller(user.getId(), sellerRequestDto);
    return ResponseEntity.ok(updatedSeller);
  }
}

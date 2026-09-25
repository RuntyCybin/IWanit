package com.nicodev.iwanit.controller;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nicodev.iwanit.model.dto.OfferRequestDto;
import com.nicodev.iwanit.model.dto.OfferResponseDto;
import com.nicodev.iwanit.service.OfferService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/v1/offers")
@AllArgsConstructor
public class OfferController {

  private final OfferService offerService;

  @PostMapping
  public ResponseEntity<OfferResponseDto> createOffer(@RequestBody OfferRequestDto offerRequestDto) {
    var createdOffer = this.offerService.createOffer(offerRequestDto);
    return ResponseEntity.created(URI.create("/v1/offers/" + createdOffer.id())).body(createdOffer);
  }

  @GetMapping("/{id}")
  public ResponseEntity<OfferResponseDto> getOffer(@PathVariable Long id) {
    return ResponseEntity.ok(this.offerService.getOffer(id));
  }

  @GetMapping("/seller/{sellerId}")
  public ResponseEntity<List<OfferResponseDto>> getAllSellerOffers(@PathVariable Long sellerId) {
    return ResponseEntity.ok(this.offerService.getAllSellerOffers(sellerId));
  }

  @PutMapping("/{id}")
  public ResponseEntity<OfferResponseDto> updateOffer(@PathVariable Long id,
      @RequestBody OfferRequestDto offerRequestDto) {
    return ResponseEntity.ok(this.offerService.updateOffer(id, offerRequestDto));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteOffer(@PathVariable Long id) {
    this.offerService.deleteOffer(id);
    return ResponseEntity.noContent().build();
  }
}

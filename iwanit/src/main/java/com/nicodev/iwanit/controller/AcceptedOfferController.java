package com.nicodev.iwanit.controller;

import java.net.URI;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nicodev.iwanit.model.dto.AcceptedOfferRequestDto;
import com.nicodev.iwanit.model.dto.AcceptedOfferResponseDto;
import com.nicodev.iwanit.service.AcceptedOfferService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/v1/accepted-offers")
@AllArgsConstructor
public class AcceptedOfferController {

  private final AcceptedOfferService acceptedOfferService;

  /**
   * Buyer accepts an offer from the Article view
   */
  @PostMapping
  public ResponseEntity<AcceptedOfferResponseDto> createAcceptedOffer(
      @RequestBody AcceptedOfferRequestDto acceptedOfferRequestDto) {
    var createdAcceptedOffer = this.acceptedOfferService.createAcceptedOffer(acceptedOfferRequestDto);
    return ResponseEntity.created(URI.create("/v1/accepted-offers/" + createdAcceptedOffer.id()))
        .body(createdAcceptedOffer);
  }

  @GetMapping("/{id}")
  public ResponseEntity<AcceptedOfferResponseDto> getAcceptedOffer(@PathVariable Long id) {
    return ResponseEntity.ok(this.acceptedOfferService.getAcceptedOffer(id));
  }
}

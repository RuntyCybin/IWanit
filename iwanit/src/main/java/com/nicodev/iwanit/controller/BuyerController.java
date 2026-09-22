package com.nicodev.iwanit.controller;

import java.net.URI;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nicodev.iwanit.model.dto.BuyerRequestDto;
import com.nicodev.iwanit.model.dto.BuyerResponseDto;
import com.nicodev.iwanit.service.BuyerService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/v1/buyers")
@AllArgsConstructor
public class BuyerController {

  private final Logger log = LoggerFactory.getLogger(BuyerController.class);
  private final BuyerService buyerService;

  /**
   * Creates a buyer related to a user
   *
   * @param buyerRequestDto the buyer request DTO
   * @return the created buyer response DTO
   */
  @PostMapping
  public ResponseEntity<BuyerResponseDto> createBuyer(
      @RequestBody BuyerRequestDto buyerRequestDto) {
    BuyerResponseDto createdBuyer = this.buyerService.createBuyer(buyerRequestDto);
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
  public ResponseEntity<Void> deleteBuyer(@PathVariable Long id) {
    this.buyerService.deleteBuyer(id);
    return ResponseEntity.noContent().build();
  }
}

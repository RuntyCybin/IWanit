package com.nicodev.iwanit.controller;

import java.util.Arrays;
import java.util.List;

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

@RestController
@RequestMapping("/v1/buyers")
public class BuyerController {

  @PostMapping
  public ResponseEntity<BuyerResponseDto> createBuyer(
      @RequestBody BuyerRequestDto buyerRequestDto) {
    BuyerResponseDto buyerResponseDto = new BuyerResponseDto(1L, "John Doe", "john.doe@example.com", "123-456-7890");
    return ResponseEntity.ok(buyerResponseDto);
  }

  @GetMapping("/{id}")
  public ResponseEntity<BuyerResponseDto> getBuyer(@PathVariable Long id) {
    BuyerResponseDto buyerResponseDto = new BuyerResponseDto(1L, "John Doe", "john.doe@example.com", "123-456-7890");
    return ResponseEntity.ok(buyerResponseDto);
  }

  @GetMapping
  public ResponseEntity<List<BuyerResponseDto>> getAllBuyers() {
    List<BuyerResponseDto> buyerResponseDtos = Arrays.asList(
        new BuyerResponseDto(1L, "John Doe", "john.doe@example.com", "123-456-7890"),
        new BuyerResponseDto(2L, "Jane Smith", "jane.smith@example.com", "098-765-4321"),
        new BuyerResponseDto(3L, "Bob Johnson", "bob.johnson@example.com", "555-555-5555"));
    return ResponseEntity.ok(buyerResponseDtos);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<BuyerResponseDto> deleteBuyer(@PathVariable Long id) {
    BuyerResponseDto buyerResponseDto = new BuyerResponseDto(1L, "John Doe", "john.doe@example.com", "123-456-7890");
    return ResponseEntity.ok(buyerResponseDto);
  }
}

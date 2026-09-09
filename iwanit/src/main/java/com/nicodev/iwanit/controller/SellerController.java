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

import com.nicodev.iwanit.model.dto.SellerRequestDto;
import com.nicodev.iwanit.model.dto.SellerResponseDto;

@RestController
@RequestMapping("/v1/sellers")
public class SellerController {

  @PostMapping
  public ResponseEntity<SellerResponseDto> createSeller(@RequestBody SellerRequestDto sellerRequestDto) {
    SellerResponseDto sellerResponseDto = new SellerResponseDto(1L, sellerRequestDto.name(), sellerRequestDto.email(),
        sellerRequestDto.phoneNumber());
    return ResponseEntity.ok(sellerResponseDto);
  }

  @GetMapping("/{id}")
  public ResponseEntity<SellerResponseDto> getSeller(@PathVariable Long id) {
    SellerResponseDto sellerResponseDto = new SellerResponseDto(1L, "John Doe", "john.doe@example.com", "123-456-7890");
    return ResponseEntity.ok(sellerResponseDto);
  }

  @GetMapping
  public ResponseEntity<List<SellerResponseDto>> getAllSellers() {
    List<SellerResponseDto> sellerResponseDtos = Arrays.asList(
        new SellerResponseDto(1L, "John Doe", "john.doe@example.com", "123-456-7890"),
        new SellerResponseDto(2L, "Jane Smith", "jane.smith@example.com", "098-765-4321"),
        new SellerResponseDto(3L, "Bob Johnson", "bob.johnson@example.com", "555-555-5555"));
    return ResponseEntity.ok(sellerResponseDtos);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<String> deleteSeller(@PathVariable Long id) {
    return ResponseEntity.ok("Seller deleted successfully");
  }
}

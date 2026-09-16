package com.nicodev.iwanit.controller;

import java.net.URI;
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
import com.nicodev.iwanit.service.SellerService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/v1/sellers")
@AllArgsConstructor
public class SellerController {

  private final SellerService sellerService;

  @PostMapping
  public ResponseEntity<SellerResponseDto> createSeller(@RequestBody SellerRequestDto sellerRequestDto) {
    var createdSeller = this.sellerService.createSeller(sellerRequestDto);
    return ResponseEntity.created(URI.create("/v1/sellers/" + createdSeller.id())).body(createdSeller);
  }

  @GetMapping("/{id}")
  public ResponseEntity<SellerResponseDto> getSeller(@PathVariable Long id) {
    return ResponseEntity.ok(this.sellerService.getSellerById(id));
  }

  @GetMapping
  public ResponseEntity<List<SellerResponseDto>> getAllSellers() {
    return ResponseEntity.ok(this.sellerService.getAllSellers());
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteSeller(@PathVariable Long id) {
    this.sellerService.deleteSeller(id);
    return ResponseEntity.noContent().build();
  }
}

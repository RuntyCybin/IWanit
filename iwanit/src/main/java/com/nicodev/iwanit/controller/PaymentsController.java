package com.nicodev.iwanit.controller;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nicodev.iwanit.model.dto.PaymentRequestDto;
import com.nicodev.iwanit.model.dto.PaymentResponseDto;
import com.nicodev.iwanit.service.PaymentService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/v1/payments")
@AllArgsConstructor
public class PaymentsController {

  private final PaymentService paymentService;

  /**
   * Buyer pays an accepted offer from the Compra view
   */
  @PostMapping
  public ResponseEntity<PaymentResponseDto> createPayment(@RequestBody PaymentRequestDto paymentRequestDto) {
    var createdPayment = this.paymentService.createPayment(paymentRequestDto);
    return ResponseEntity.created(URI.create("/v1/payments/" + createdPayment.id())).body(createdPayment);
  }

  @GetMapping("/{paymentId}")
  public ResponseEntity<PaymentResponseDto> getPaymentById(@PathVariable Long paymentId) {
    return ResponseEntity.ok(this.paymentService.getPaymentById(paymentId));
  }

  @GetMapping("/buyer/{buyerId}")
  public ResponseEntity<List<PaymentResponseDto>> getAllBuyerPayments(@PathVariable Long buyerId) {
    return ResponseEntity.ok(this.paymentService.getAllBuyerPayments(buyerId));
  }

}

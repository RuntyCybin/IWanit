package com.nicodev.iwanit.model.dto;

import com.nicodev.iwanit.model.PaymentMethod;

public record PaymentRequestDto(Long buyerId, Long offerId, PaymentMethod paymentMethod) {

  public PaymentRequestDto {
    if (buyerId == null) {
      throw new IllegalArgumentException("Buyer ID must not be null");
    }
    if (offerId == null) {
      throw new IllegalArgumentException("Offer ID must not be null");
    }
    if (paymentMethod == null) {
      throw new IllegalArgumentException("Payment method must not be null");
    }
  }

}

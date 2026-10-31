package com.nicodev.iwanit.exception;

public class PaymentAlreadyCompletedException extends RuntimeException {
  public PaymentAlreadyCompletedException(Long offerId) {
    super("Offer with ID " + offerId + " has already been paid");
  }
}

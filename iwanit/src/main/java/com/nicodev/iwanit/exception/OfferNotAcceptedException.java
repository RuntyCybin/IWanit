package com.nicodev.iwanit.exception;

public class OfferNotAcceptedException extends RuntimeException {
  public OfferNotAcceptedException(Long id) {
    super("Offer with ID " + id + " has not been accepted");
  }
}

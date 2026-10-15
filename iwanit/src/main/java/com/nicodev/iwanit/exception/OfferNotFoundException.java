package com.nicodev.iwanit.exception;

public class OfferNotFoundException extends RuntimeException {
  public OfferNotFoundException(Long id) {
    super("Offer not found with ID: " + id);
  }
}

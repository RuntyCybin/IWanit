package com.nicodev.iwanit.exception;

public class AcceptedOfferNotFoundException extends RuntimeException {
  public AcceptedOfferNotFoundException(Long id) {
    super("Accepted offer not found with ID: " + id);
  }
}

package com.nicodev.iwanit.exception;

public class OfferNotPendingException extends RuntimeException {
  public OfferNotPendingException(Long id) {
    super("Offer with ID " + id + " is no longer pending");
  }
}

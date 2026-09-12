package com.nicodev.iwanit.exception;

public class BuyerNotFoundException extends RuntimeException {
  public BuyerNotFoundException(Long id, String reason) {
    super("Buyer not found with ID: " + id + " - " + reason);
  }
}

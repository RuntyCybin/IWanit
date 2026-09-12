package com.nicodev.iwanit.exception;

public class BuyerNotSavedException extends RuntimeException {
  public BuyerNotSavedException(String email, String message) {
    super("Error saving buyer with email " + email + ": " + message);
  }
}

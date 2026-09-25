package com.nicodev.iwanit.exception;

public class BuyerNotDeletedException extends RuntimeException {
  public BuyerNotDeletedException(Long id, String reason) {
    super("Buyer with id " + id + " could not be deleted: " + reason);
  }
}

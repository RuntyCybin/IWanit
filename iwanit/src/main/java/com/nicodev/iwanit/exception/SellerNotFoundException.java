package com.nicodev.iwanit.exception;

public class SellerNotFoundException extends RuntimeException {
  public SellerNotFoundException(Long id) {
    super("Seller with ID " + id + " not found");
  }
}

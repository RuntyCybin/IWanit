package com.nicodev.iwanit.exception;

public class UserNotFoundByEmailException extends RuntimeException {
  public UserNotFoundByEmailException(String email) {
    super("User not found with email: " + email);
  }
}

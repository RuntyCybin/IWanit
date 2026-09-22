package com.nicodev.iwanit.exception;

public class UserNotFoundException extends RuntimeException {
  public UserNotFoundException(String msg) {
    super("User not found with id: " + msg);
  }
}

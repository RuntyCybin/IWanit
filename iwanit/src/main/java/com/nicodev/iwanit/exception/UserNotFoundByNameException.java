package com.nicodev.iwanit.exception;

public class UserNotFoundByNameException extends RuntimeException {
  public UserNotFoundByNameException(String username) {
    super("User not found by the username: " + username);
  }
}

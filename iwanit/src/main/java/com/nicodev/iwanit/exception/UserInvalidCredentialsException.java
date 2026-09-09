package com.nicodev.iwanit.exception;

public class UserInvalidCredentialsException extends RuntimeException {
  public UserInvalidCredentialsException(String username) {
    super("Invalid username or password for user: " + username);
  }
}

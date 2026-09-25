package com.nicodev.iwanit.exception;

public class UserNotSavedException extends RuntimeException {
  public UserNotSavedException(String username, String message) {
    super("Error saving user with username " + username + ": " + message);
  }
}

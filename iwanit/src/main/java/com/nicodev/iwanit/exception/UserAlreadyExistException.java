package com.nicodev.iwanit.exception;

public class UserAlreadyExistException extends RuntimeException {
  public UserAlreadyExistException(String username) {
    super("User already exists: " + username);
  }
}

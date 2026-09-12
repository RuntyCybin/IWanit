package com.nicodev.iwanit.exception;

public class ArticleNotSavedException extends RuntimeException {
  public ArticleNotSavedException(String message) {
    super("Failed to create article" + (message != null ? ": " + message : ""));
  }
}

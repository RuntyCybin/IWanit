package com.nicodev.iwanit.exception;

public class ArticleNotAvailableException extends RuntimeException {
  public ArticleNotAvailableException(Long id) {
    super("Article with ID " + id + " is no longer available");
  }
}

package com.nicodev.iwanit.exception;

public class ArticleNotDeletedException extends RuntimeException {
  public ArticleNotDeletedException(Long id, Throwable cause) {
    super("Failed to delete article with ID: " + id + ". Cause: " + cause.getMessage(), cause);
  }
}

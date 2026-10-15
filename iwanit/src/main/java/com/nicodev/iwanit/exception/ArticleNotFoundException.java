package com.nicodev.iwanit.exception;

public class ArticleNotFoundException extends RuntimeException {
  public ArticleNotFoundException(Long id) {
    super("Article not found with ID: " + id);
  }
}

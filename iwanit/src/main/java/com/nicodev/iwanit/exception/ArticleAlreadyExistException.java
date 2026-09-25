package com.nicodev.iwanit.exception;

public class ArticleAlreadyExistException extends RuntimeException {
  public ArticleAlreadyExistException(String message) {
    super("Article with title '" + message + "' already exists");
  }
}

package com.nicodev.iwanit.exception;

public class AcceptedOfferAlreadyExistException extends RuntimeException {
  public AcceptedOfferAlreadyExistException(Long articleId) {
    super("Article with ID " + articleId + " already has an accepted offer");
  }
}

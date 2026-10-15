package com.nicodev.iwanit.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

  /**
   * Handles UserNotFoundByNameException and returns a 404 Not Found response.
   * Exception is thrown when a user is not found by their username.
   * 
   * @param ex
   * @return
   */
  @ExceptionHandler(UserNotFoundByNameException.class)
  public ResponseEntity<String> handleUserNotFoundByNameException(UserNotFoundByNameException ex) {
    return ResponseEntity
        .status(HttpStatus.NOT_FOUND)
        .body(ex.getMessage());
  }

  /**
   * Handles UserNotFoundByEmailException and returns a 404 Not Found response.
   * Exception is thrown when a user is not found by their email.
   * 
   * @param ex
   * @return
   */
  @ExceptionHandler(UserNotFoundException.class)
  public ResponseEntity<String> handleUserNotFoundByEmailException(UserNotFoundException ex) {
    return ResponseEntity
        .status(HttpStatus.NOT_FOUND)
        .body(ex.getMessage());
  }

  /**
   * Handles UserInvalidCredentialsException and returns a 401 Unauthorized
   * response.
   * Exception is thrown when a user provides invalid credentials during login.
   * 
   * @param ex
   * @return
   */
  @ExceptionHandler(UserInvalidCredentialsException.class)
  public ResponseEntity<String> handleUserInvalidCredentialsException(UserInvalidCredentialsException ex) {
    return ResponseEntity
        .status(HttpStatus.UNAUTHORIZED)
        .body(ex.getMessage());
  }

  /**
   * Handles UserAlreadyExistException and returns a 409 Conflict response.
   * Exception is thrown when a user tries to register with an existing username.
   * 
   * @param ex
   * @return
   */
  @ExceptionHandler(UserAlreadyExistException.class)
  public ResponseEntity<String> handleUserAlreadyExistException(UserAlreadyExistException ex) {
    return ResponseEntity
        .status(HttpStatus.CONFLICT)
        .body(ex.getMessage());
  }

  /**
   * Handles UserNotSavedException and returns a 500 Internal Server Error
   * response.
   * Exception is thrown when a user could not be saved to the database.
   * 
   * @param ex
   * @return
   */
  @ExceptionHandler(UserNotSavedException.class)
  public ResponseEntity<String> handleUserNotSavedException(UserNotSavedException ex) {
    return ResponseEntity
        .status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body(ex.getMessage());
  }

  /**
   * Handles BuyerNotSavedException and returns a 500 Internal Server Error
   * response.
   * Exception is thrown when a buyer could not be saved to the database.
   * 
   * @param ex
   * @return
   */
  @ExceptionHandler(BuyerNotSavedException.class)
  public ResponseEntity<String> handleBuyerNotSavedException(BuyerNotSavedException ex) {
    return ResponseEntity
        .status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body(ex.getMessage());
  }

  /**
   * Handles ArticleInvalidRequestException and returns a 400 Bad Request
   * response.
   * Exception is thrown when an article request has invalid data.
   * 
   * @param ex
   * @return
   */
  @ExceptionHandler(ArticleInvalidRequestException.class)
  public ResponseEntity<String> handleArticleInvalidRequestException(ArticleInvalidRequestException ex) {
    return ResponseEntity
        .status(HttpStatus.BAD_REQUEST)
        .body(ex.getMessage());
  }

  /**
   * Handles ArticleNotSavedException and returns a 500 Internal Server Error
   * response.
   * Exception is thrown when an article could not be saved to the database.
   * 
   * @param ex
   * @return
   */
  @ExceptionHandler(ArticleNotSavedException.class)
  public ResponseEntity<String> handleArticleNotSavedException(ArticleNotSavedException ex) {
    return ResponseEntity
        .status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body(ex.getMessage());
  }

  /**
   * Handles ArticleAlreadyExistException and returns a 409 Conflict response.
   * Exception is thrown when an article with the same title already exists.
   * 
   * @param ex
   * @return
   */
  @ExceptionHandler(ArticleAlreadyExistException.class)
  public ResponseEntity<String> handleArticleAlreadyExistException(ArticleAlreadyExistException ex) {
    return ResponseEntity
        .status(HttpStatus.CONFLICT)
        .body(ex.getMessage());
  }

  /**
   * Handles ArticleNotDeletedException and returns a 500 Internal Server Error
   * response.
   * Exception is thrown when an article could not be deleted from the database.
   * 
   * @param ex
   * @return
   */
  @ExceptionHandler(ArticleNotDeletedException.class)
  public ResponseEntity<String> handleArticleNotDeletedException(ArticleNotDeletedException ex) {
    return ResponseEntity
        .status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body(ex.getMessage());
  }

  /**
   * Handles AcceptedOfferAlreadyExistException and returns a 409 Conflict
   * response.
   * Exception is thrown when the buyer tries to accept an offer on an article
   * that already has an accepted offer.
   *
   * @param ex
   * @return
   */
  @ExceptionHandler(AcceptedOfferAlreadyExistException.class)
  public ResponseEntity<String> handleAcceptedOfferAlreadyExistException(AcceptedOfferAlreadyExistException ex) {
    return ResponseEntity
        .status(HttpStatus.CONFLICT)
        .body(ex.getMessage());
  }

  @ExceptionHandler(OfferAlreadyExistsException.class)
  public ResponseEntity<String> handleOfferAlreadyExistsException(OfferAlreadyExistsException ex) {
    return ResponseEntity
        .status(HttpStatus.CONFLICT)
        .body(ex.getMessage());
  }

  /**
   * Handles OfferNotPendingException and returns a 409 Conflict response.
   * Exception is thrown when an offer that was already accepted or rejected is
   * accepted, modified or deleted.
   *
   * @param ex
   * @return
   */
  @ExceptionHandler(OfferNotPendingException.class)
  public ResponseEntity<String> handleOfferNotPendingException(OfferNotPendingException ex) {
    return ResponseEntity
        .status(HttpStatus.CONFLICT)
        .body(ex.getMessage());
  }

  /**
   * Handles ArticleNotAvailableException and returns a 409 Conflict response.
   * Exception is thrown when an article that was already bought receives a new
   * offer or is modified or deleted.
   *
   * @param ex
   * @return
   */
  @ExceptionHandler(ArticleNotAvailableException.class)
  public ResponseEntity<String> handleArticleNotAvailableException(ArticleNotAvailableException ex) {
    return ResponseEntity
        .status(HttpStatus.CONFLICT)
        .body(ex.getMessage());
  }

  /**
   * Handles AccessDeniedException and returns a 403 Forbidden response.
   * Exception is thrown when the authenticated user tries to act on a resource
   * owned by another user.
   * 
   * @param ex
   * @return
   */
  @ExceptionHandler(AccessDeniedException.class)
  public ResponseEntity<String> handleAccessDeniedException(AccessDeniedException ex) {
    return ResponseEntity
        .status(HttpStatus.FORBIDDEN)
        .body(ex.getMessage());
  }

  /**
   * Handles HttpMessageNotReadableException and returns a 400 Bad Request
   * response.
   * Exception is thrown when the request body is malformed or a request DTO
   * fails its validation while being deserialized.
   *
   * @param ex
   * @return
   */
  @ExceptionHandler(HttpMessageNotReadableException.class)
  public ResponseEntity<String> handleMessageNotReadaEntityException(HttpMessageNotReadableException ex) {
    return ResponseEntity
        .status(HttpStatus.BAD_REQUEST)
        .body(ex.getMostSpecificCause().getMessage());
  }

  /**
   * Handles IllegalArgumentException and returns a 400 Bad Request response.
   * Exception is thrown when a request is not valid for the current state, e.g.
   * changing the article or the seller of an offer.
   *
   * @param ex
   * @return
   */
  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException ex) {
    return ResponseEntity
        .status(HttpStatus.BAD_REQUEST)
        .body(ex.getMessage());
  }

  /**
   * NOT FOUND EXCEPTIONS
   * 
   */

  @ExceptionHandler(ArticleNotFoundException.class)
  public ResponseEntity<String> handleArticleNotFoundException(ArticleNotFoundException ex) {
    return ResponseEntity
        .status(HttpStatus.NOT_FOUND)
        .body(ex.getMessage());
  }

  @ExceptionHandler(OfferNotFoundException.class)
  public ResponseEntity<String> handleOfferNotFoundException(OfferNotFoundException ex) {
    return ResponseEntity
        .status(HttpStatus.NOT_FOUND)
        .body(ex.getMessage());
  }

  @ExceptionHandler(AcceptedOfferNotFoundException.class)
  public ResponseEntity<String> handleAcceptedOfferNotFoundException(AcceptedOfferNotFoundException ex) {
    return ResponseEntity
        .status(HttpStatus.NOT_FOUND)
        .body(ex.getMessage());
  }

  /**
   * Handles BuyerNotFoundException and returns a 404 Not Found response.
   * Exception is thrown when a buyer is not found by their id.
   * 
   * @param ex
   * @return
   */
  @ExceptionHandler(BuyerNotFoundException.class)
  public ResponseEntity<String> handleBuyerNotFoundException(BuyerNotFoundException ex) {
    return ResponseEntity
        .status(HttpStatus.NOT_FOUND)
        .body(ex.getMessage());
  }

  @ExceptionHandler(SellerNotFoundException.class)
  public ResponseEntity<String> handleSellerNotFoundException(SellerNotFoundException ex) {
    return ResponseEntity
        .status(HttpStatus.NOT_FOUND)
        .body(ex.getMessage());
  }

}

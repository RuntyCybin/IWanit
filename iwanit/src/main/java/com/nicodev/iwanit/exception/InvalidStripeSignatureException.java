package com.nicodev.iwanit.exception;

public class InvalidStripeSignatureException extends RuntimeException {
  public InvalidStripeSignatureException() {
    super("Invalid Stripe webhook signature");
  }
}

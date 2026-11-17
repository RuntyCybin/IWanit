package com.nicodev.iwanit.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.nicodev.iwanit.exception.InvalidStripeSignatureException;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.Event;
import com.stripe.net.Webhook;

@Service
public class StripeWebhookVerifier {

  @Value("${stripe.webhook.secret}")
  private String webhookSecret;

  /**
   * Checks the Stripe-Signature header against the raw payload and parses the
   * event. The webhook endpoint is public, so this is the only proof that the
   * request comes from Stripe.
   */
  public Event verify(String payload, String signature) {
    try {
      return Webhook.constructEvent(payload, signature, webhookSecret);
    } catch (SignatureVerificationException e) {
      throw new InvalidStripeSignatureException();
    }
  }
}

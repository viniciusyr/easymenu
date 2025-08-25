package com.easymenu.payment.gateway.stripe;

public class StripePaymentException extends RuntimeException {
  public StripePaymentException(String message) {
    super(message);
  }
}

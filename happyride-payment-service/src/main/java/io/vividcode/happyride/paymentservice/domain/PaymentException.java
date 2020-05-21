package io.vividcode.happyride.paymentservice.domain;

public class PaymentException extends RuntimeException {

  public PaymentException(final String message) {
    super(message);
  }
}

package io.vividcode.happyride.paymentservice.domain;

public class PaymentNotFoundException extends RuntimeException {

  private final String tripId;

  public PaymentNotFoundException(final String tripId) {
    this.tripId = tripId;
  }

  @Override
  public String getMessage() {
    return String.format("Cannot find the payment for trip %s", this.tripId);
  }
}

package io.vividcode.happyride.tripvalidationservice.service;

public class TripDistanceTooLongException extends TripValidationException {

  private final double value;
  private final double limit;

  public TripDistanceTooLongException(final double value, final double limit) {
    this.value = value;
    this.limit = limit;
  }

  @Override
  public String getMessage() {
    return String
        .format("Distance is too long, current value is [%s], limit is [%s]",
            this.value, this.limit);
  }
}

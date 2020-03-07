package io.vividcode.happyride.tripvalidationservice.service;

public class TripDistanceTooLongException extends TripValidationException {
  private final double value;
  private final double limit;

  public TripDistanceTooLongException(double value, double limit) {
    this.value = value;
    this.limit = limit;
  }

  @Override
  public String getMessage() {
    return String.format("行程距离过长，当前值 [%s]，上限值 [%s]", value, limit);
  }
}

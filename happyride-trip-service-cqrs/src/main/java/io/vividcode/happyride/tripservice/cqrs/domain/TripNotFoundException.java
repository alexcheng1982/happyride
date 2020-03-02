package io.vividcode.happyride.tripservice.cqrs.domain;

public class TripNotFoundException extends RuntimeException {
  private final String tripId;

  public TripNotFoundException(String tripId) {
    this.tripId = tripId;
  }

  @Override
  public String getMessage() {
    return String.format("Trip %s not found", tripId);
  }
}

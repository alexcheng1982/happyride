package io.vividcode.happyride.tripservice.service;

import io.vividcode.happyride.tripservice.api.TripState;

public class IllegalTripStateException extends RuntimeException {

  private final TripState requiredState;

  public IllegalTripStateException(
      TripState requiredState) {
    this.requiredState = requiredState;
  }

  @Override
  public String getMessage() {
    return String.format("Trip must be in %s state", requiredState);
  }
}

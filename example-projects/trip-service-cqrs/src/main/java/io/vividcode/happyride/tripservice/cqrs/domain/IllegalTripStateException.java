package io.vividcode.happyride.tripservice.cqrs.domain;

import io.vividcode.happyride.tripservice.api.TripState;

public class IllegalTripStateException extends RuntimeException {

  private final TripState fromState;
  private final TripState toState;
  
  public IllegalTripStateException(TripState fromState,
      TripState toState) {
    this.fromState = fromState;
    this.toState = toState;
  }

  @Override
  public String getMessage() {
    return String
        .format("Cannot change trip state from %s to %s", fromState, toState);
  }
}

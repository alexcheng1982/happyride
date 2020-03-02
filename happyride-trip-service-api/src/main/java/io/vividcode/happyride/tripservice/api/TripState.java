package io.vividcode.happyride.tripservice.api;

public enum TripState {
  CREATED,
  REJECTED,
  CONFIRMED,
  PENDING_DISPATCH,
  DISPATCHED,
  ACCEPTED,
  FAILED,
  CANCELLED,
  STARTED,
  FINISHED
}

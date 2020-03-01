package io.vividcode.happyride.tripservice.domain;

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

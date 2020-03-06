package io.vividcode.happyride.tripservice.api;

public enum TripState {
  CREATED,
  REJECTED,
  CONFIRMED,
  DISPATCHED,
  ACCEPTED,
  FAILED,
  CANCELLATION_PENDING,
  CANCELLED,
  CANCELLATION_REJECTED,
  STARTED,
  FINISHED
}

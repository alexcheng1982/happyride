package io.vividcode.happyride.tripvalidationservice.service;

public class PassengerBlockedException extends TripValidationException {

  private final String passengerId;

  public PassengerBlockedException(final String passengerId) {
    this.passengerId = passengerId;
  }

  @Override
  public String getMessage() {
    return String.format("Passenger [%s] is blocked", this.passengerId);
  }
}

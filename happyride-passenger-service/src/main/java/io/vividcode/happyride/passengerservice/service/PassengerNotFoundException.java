package io.vividcode.happyride.passengerservice.service;

public class PassengerNotFoundException extends RuntimeException {

  private final String passengerId;

  public PassengerNotFoundException(String passengerId) {
    this.passengerId = passengerId;
  }

  @Override
  public String getMessage() {
    return String.format("Passenger %s not found", passengerId);
  }
}

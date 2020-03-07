package io.vividcode.happyride.tripvalidationservice.service;

public class PassengerBlockedException extends TripValidationException {
  private final String passengerId;

  public PassengerBlockedException(String passengerId) {
    this.passengerId = passengerId;
  }

  @Override
  public String getMessage() {
    return String.format("乘客 [%s] 已经被禁用", passengerId);
  }
}

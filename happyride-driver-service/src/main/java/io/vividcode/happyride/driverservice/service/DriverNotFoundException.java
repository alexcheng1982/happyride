package io.vividcode.happyride.driverservice.service;

public class DriverNotFoundException extends RuntimeException {

  private final String driverId;

  public DriverNotFoundException(String driverId) {
    this.driverId = driverId;
  }

  @Override
  public String getMessage() {
    return String.format("Driver %s not found", driverId);
  }
}

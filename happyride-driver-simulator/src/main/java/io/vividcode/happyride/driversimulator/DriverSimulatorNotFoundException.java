package io.vividcode.happyride.driversimulator;

public class DriverSimulatorNotFoundException extends RuntimeException {
  private final String driverSimulatorId;

  public DriverSimulatorNotFoundException(String driverSimulatorId) {
    this.driverSimulatorId = driverSimulatorId;
  }

  @Override
  public String getMessage() {
    return String.format("Driver simulator %s not found", driverSimulatorId);
  }
}

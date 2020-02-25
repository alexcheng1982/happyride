package io.vividcode.happyride.driversimulator;

import io.vividcode.happyride.dispatcherservice.api.events.DriverLocation;
import org.axonframework.eventhandling.gateway.EventGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DriverSimulatorFactory {
  @Autowired
  EventGateway eventGateway;

  public DriverSimulator create(String driverId, String vehicleId) {
    return new DriverSimulator(eventGateway,
        new DriverLocation(driverId, vehicleId, 0.0, 0.0));
  }
}

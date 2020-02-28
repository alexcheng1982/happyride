package io.vividcode.happyride.driversimulator;

import io.vividcode.happyride.dispatcherservice.api.events.DriverLocation;
import java.math.BigDecimal;
import org.axonframework.eventhandling.gateway.EventGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DriverSimulatorFactory {
  @Autowired
  EventGateway eventGateway;

  public DriverSimulator create(String driverId, String vehicleId) {
    return new DriverSimulator(driverId, vehicleId, eventGateway,
        new DriverLocation(driverId, vehicleId, BigDecimal.ZERO, BigDecimal.ZERO));
  }
}

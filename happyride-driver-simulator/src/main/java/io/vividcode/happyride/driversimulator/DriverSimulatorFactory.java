package io.vividcode.happyride.driversimulator;

import io.vividcode.happyride.driverservice.api.events.DriverLocation;
import java.math.BigDecimal;
import org.axonframework.eventhandling.gateway.EventGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DriverSimulatorFactory {
  @Autowired
  EventGateway eventGateway;

  public DriverSimulator create(String driverId) {
    return new DriverSimulator(new DriverLocation(driverId, BigDecimal.ZERO, BigDecimal.ZERO), eventGateway);
  }
}

package io.vividcode.happyride.driversimulator;

import io.vividcode.happyride.driverservice.api.events.DriveLocationUpdatedEvent;
import io.vividcode.happyride.driverservice.api.events.DriverLocation;
import java.math.BigDecimal;
import org.axonframework.eventhandling.gateway.EventGateway;

public class DriverSimulator {
  private final DriverLocation initialLocation;
  private final EventGateway eventGateway;

  public DriverSimulator(DriverLocation initialLocation, EventGateway eventGateway) {
    this.initialLocation = initialLocation;
    this.eventGateway = eventGateway;
  }

  public void startSimulation() {
    for (int i = 0; i < 10; i++) {
      sendPosition(initialLocation.moveTo(BigDecimal.valueOf(0.01 * i), BigDecimal.valueOf(0.01 * i)));
    }
  }

  private void sendPosition(DriverLocation location) {
    DriveLocationUpdatedEvent event = new DriveLocationUpdatedEvent();
    event.setTimestamp(System.currentTimeMillis());
    event.setDriverLocation(location);
    eventGateway.publish(event);
  }
}

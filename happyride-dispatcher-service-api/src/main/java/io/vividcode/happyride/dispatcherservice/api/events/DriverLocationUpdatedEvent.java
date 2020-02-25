package io.vividcode.happyride.dispatcherservice.api.events;

import lombok.Data;

@Data
public class DriverLocationUpdatedEvent {
  private DriverLocation driverLocation;

  private long timestamp;
}

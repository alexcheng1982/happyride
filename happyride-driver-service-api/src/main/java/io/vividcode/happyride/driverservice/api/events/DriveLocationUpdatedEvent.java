package io.vividcode.happyride.driverservice.api.events;

import lombok.Data;

@Data
public class DriveLocationUpdatedEvent {
  private DriverLocation driverLocation;

  private long timestamp;
}

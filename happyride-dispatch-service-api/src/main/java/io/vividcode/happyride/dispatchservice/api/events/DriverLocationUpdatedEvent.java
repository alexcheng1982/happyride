package io.vividcode.happyride.dispatchservice.api.events;

import io.vividcode.happyride.common.DriverState;
import lombok.Data;

@Data
public class DriverLocationUpdatedEvent {

  private DriverLocation location;

  private DriverState state;

  private long timestamp;
}

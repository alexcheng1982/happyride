package io.vividcode.happyride.dispatchservice;

import io.vividcode.happyride.common.DriverState;
import io.vividcode.happyride.dispatchservice.api.events.DriverLocation;
import io.vividcode.happyride.dispatchservice.api.events.DriverLocationUpdatedEvent;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DriverLocationUpdater {

  @Autowired
  DriverLocationService driverLocationService;

  @EventHandler
  public void handle(final DriverLocationUpdatedEvent event) {
    final DriverLocation location = event.getLocation();
    if (event.getState() == DriverState.AVAILABLE) {
      this.driverLocationService.addAvailableDriver(location);
    } else {
      this.driverLocationService.removeAvailableDriver(location.getDriverId());
    }
  }
}

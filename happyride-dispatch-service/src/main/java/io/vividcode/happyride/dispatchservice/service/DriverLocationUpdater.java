package io.vividcode.happyride.dispatchservice.service;

import io.vividcode.happyride.common.DriverState;
import io.vividcode.happyride.dispatchservice.api.events.DriverLocation;
import io.vividcode.happyride.dispatchservice.api.events.DriverLocationUpdatedEvent;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DriverLocationUpdater {

  @Autowired
  DispatcherService dispatcherService;

  @EventHandler
  public void handle(DriverLocationUpdatedEvent event) {
    DriverLocation location = event.getLocation();
    if (event.getState() == DriverState.AVAILABLE) {
      dispatcherService.addAvailableDriver(location);
    } else {
      dispatcherService.removeAvailableDriver(location.getDriverId());
    }
  }
}

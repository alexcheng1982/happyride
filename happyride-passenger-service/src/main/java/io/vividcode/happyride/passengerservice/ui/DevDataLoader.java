package io.vividcode.happyride.passengerservice.ui;

import io.vividcode.happyride.passengerservice.service.PassengerService;
import io.vividcode.happyride.passengerservice.support.PassengerUtils;
import java.util.concurrent.ThreadLocalRandom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@Profile("dev")
public class DevDataLoader {

  @Autowired
  PassengerService passengerService;

  @EventListener(ApplicationStartedEvent.class)
  public void loadData() {
    if (passengerService.findAll().isEmpty()) {
      for (int i = 0; i < 20; i++) {
        passengerService.createPassenger(
            PassengerUtils.buildCreatePassengerRequest(ThreadLocalRandom.current().nextInt(3)));
      }
    }
  }
}

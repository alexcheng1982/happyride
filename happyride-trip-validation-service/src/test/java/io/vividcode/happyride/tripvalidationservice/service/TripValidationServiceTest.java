package io.vividcode.happyride.tripvalidationservice.service;

import static org.junit.jupiter.api.Assertions.assertThrows;

import io.vividcode.happyride.common.Position;
import io.vividcode.happyride.common.PositionView;
import io.vividcode.happyride.tripservice.api.events.TripDetails;
import io.vividcode.happyride.tripvalidationservice.AppConfig;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;


@SpringBootTest
@ContextConfiguration(
    classes = {TripValidationService.class, AppConfig.class}
)
@EnableConfigurationProperties(AppConfig.class)
@TestPropertySource(
    properties = {
        "app.blockedPassengers[0]=passenger1",
        "app.blockedPassengers[0]=passenger2"
    }
)
public class TripValidationServiceTest {

  @Autowired
  TripValidationService tripValidationService;

  @Test
  public void testBlockingPassenger() {
    PositionView position0 = new PositionView(BigDecimal.ZERO, BigDecimal.ZERO);
    TripDetails tripDetails = new TripDetails("passenger1", position0, position0);
    assertThrows(PassengerBlockedException.class,
        () -> tripValidationService.validateTrip(tripDetails));
  }
}

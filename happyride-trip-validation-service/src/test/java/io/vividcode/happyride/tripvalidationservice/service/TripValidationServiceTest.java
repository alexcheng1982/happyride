package io.vividcode.happyride.tripvalidationservice.service;

import io.vividcode.happyride.common.PositionVO;
import io.vividcode.happyride.tripservice.api.events.TripDetails;
import io.vividcode.happyride.tripvalidationservice.AppConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertThrows;


@SpringBootTest
@ContextConfiguration(
    classes = {TripValidationService.class, AppConfig.class}
)
@EnableConfigurationProperties(AppConfig.class)
@TestPropertySource(
    properties = {
        "app.blockedPassengers[0]=passenger1",
        "app.blockedPassengers[1]=passenger2"
    }
)
public class TripValidationServiceTest {

  @Autowired
  TripValidationService tripValidationService;

  @Test
  public void testBlockingPassenger() {
    final PositionVO position0 = new PositionVO(BigDecimal.ZERO, BigDecimal.ZERO);
    final TripDetails tripDetails = new TripDetails("passenger1", position0, position0);
    assertThrows(PassengerBlockedException.class,
        () -> this.tripValidationService.validateTrip(tripDetails));
  }
}

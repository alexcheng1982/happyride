package io.vividcode.happyride.dispatchservice;

import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.fail;

import io.vividcode.happyride.dispatchservice.TripAcceptanceServiceTest.Configuration;
import io.vividcode.happyride.tripservice.api.events.DriverAcceptTripDetails;
import java.math.BigDecimal;
import java.time.Duration;
import java.util.concurrent.atomic.AtomicReference;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.autoconfigure.data.redis.DataRedisTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;

@DataRedisTest
@EnableAutoConfiguration
@ContextConfiguration(classes = {
    Configuration.class,
    TripAcceptanceService.class
})
@TestPropertySource(properties = {
    "embedded.redis.dockerImage=redis:6.2-alpine",
})
@DisplayName("Trip acceptance service")
@Disabled
public class TripAcceptanceServiceTest {

  @Autowired
  TripAcceptanceService tripAcceptanceService;

  @Test
  @DisplayName("Successful trip acceptance")
  public void testSuccessfulTripAcceptance() {
    String tripId = TestUtils.uuid();
    String driver1Id = TestUtils.uuid();
    this.tripAcceptanceService.addDriverToAcceptTrip(tripId,
        new DriverAcceptTripDetails(driver1Id, BigDecimal.valueOf(0.00001),
            BigDecimal.valueOf(0.00001)));
    String driver2Id = TestUtils.uuid();
    this.tripAcceptanceService.addDriverToAcceptTrip(tripId,
        new DriverAcceptTripDetails(driver2Id, BigDecimal.valueOf(0.00002),
            BigDecimal.valueOf(0.00002)));
    AtomicReference<String> selectedDriverId = new AtomicReference<>(null);
    this.tripAcceptanceService.startTripAcceptanceCheck(
        tripId,
        TestUtils.tripDetails0(),
        Duration.ofSeconds(5),
        (acceptedTripId, driverId) -> selectedDriverId.set(driverId),
        (acceptedTripId, reason) -> fail("Should find the driver to accept the trip")
    );
    await().atMost(Duration.ofSeconds(30))
        .untilAtomic(selectedDriverId, Matchers.comparesEqualTo(driver1Id));
  }

  @TestConfiguration
  static class Configuration {

    @Bean
    public TaskScheduler taskScheduler() {
      return new ThreadPoolTaskScheduler();
    }
  }
}

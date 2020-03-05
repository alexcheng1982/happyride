package io.vividcode.happyride.dispatchservice;

import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.fail;

import com.playtika.test.redis.EmbeddedRedisBootstrapConfiguration;
import com.playtika.test.redis.EmbeddedRedisDependenciesAutoConfiguration;
import io.vividcode.happyride.dispatchservice.TripAcceptanceServiceTest.Configuration;
import io.vividcode.happyride.tripservice.api.events.DriverAcceptTripDetails;
import java.math.BigDecimal;
import java.time.Duration;
import java.util.concurrent.atomic.AtomicReference;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
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
    EmbeddedRedisBootstrapConfiguration.class,
    Configuration.class,
    TripAcceptanceService.class
})
@ImportAutoConfiguration(classes = {EmbeddedRedisDependenciesAutoConfiguration.class,
    EmbeddedRedisBootstrapConfiguration.class})
@TestPropertySource(properties = {
    "embedded.redis.dockerImage=redis:5-alpine"
})
@DisplayName("行程接受服务")
public class TripAcceptanceServiceTest {

  @Autowired
  TripAcceptanceService tripAcceptanceService;

  @Test
  @DisplayName("成功的行程接受")
  public void testSuccessTripAcceptance() {
    String tripId = TestUtils.uuid();
    String driver1Id = TestUtils.uuid();
    tripAcceptanceService.addDriverToAcceptTrip(tripId,
        new DriverAcceptTripDetails(driver1Id, BigDecimal.valueOf(0.00001),
            BigDecimal.valueOf(0.00001)));
    String driver2Id = TestUtils.uuid();
    tripAcceptanceService.addDriverToAcceptTrip(tripId,
        new DriverAcceptTripDetails(driver2Id, BigDecimal.valueOf(0.00002),
            BigDecimal.valueOf(0.00002)));
    AtomicReference<String> selectedDriverId = new AtomicReference<>(null);
    tripAcceptanceService.startTripAcceptanceCheck(
        tripId,
        TestUtils.tripDetails0(),
        Duration.ofSeconds(5),
        (acceptedTripId, driverId) -> selectedDriverId.set(driverId),
        (acceptedTripId, reason) -> fail("应该找到正确的行程接受者")
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

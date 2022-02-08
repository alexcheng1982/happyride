package io.vividcode.happyride.dispatchservice;

import static org.assertj.core.api.Assertions.assertThat;

import io.vividcode.happyride.dispatchservice.api.events.DriverLocation;
import java.math.BigDecimal;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.autoconfigure.data.redis.DataRedisTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;

@DataRedisTest
@EnableAutoConfiguration
@ContextConfiguration(classes = {
    DriverLocationService.class
})
@TestPropertySource(properties = {
    "embedded.redis.dockerImage=redis:5-alpine"
})
@DisplayName("Driver location service")
public class DriverLocationServiceTest {

  @Autowired
  DriverLocationService driverLocationService;

  @Test
  @DisplayName("Find available drivers")
  public void testFindAvailableDrivers() {
    this.driverLocationService.addAvailableDriver(
        new DriverLocation("driver1", "vehicle1", BigDecimal.ZERO, BigDecimal.ZERO));
    this.driverLocationService.addAvailableDriver(
        new DriverLocation("driver2", "vehicle2", BigDecimal.valueOf(0.0001),
            BigDecimal.valueOf(0.0002)));
    Set<AvailableDriver> drivers = this.driverLocationService
        .findAvailableDrivers(BigDecimal.valueOf(0.0003), BigDecimal.valueOf(0.0004));
    assertThat(drivers).hasSize(2);
  }
}

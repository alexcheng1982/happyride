package io.vividcode.happyride.dispatchservice;

import com.playtika.test.redis.EmbeddedRedisBootstrapConfiguration;
import com.playtika.test.redis.EmbeddedRedisDependenciesAutoConfiguration;
import io.vividcode.happyride.dispatchservice.api.events.DriverLocation;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.autoconfigure.data.redis.DataRedisTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;

import java.math.BigDecimal;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@DataRedisTest
@EnableAutoConfiguration
@ContextConfiguration(classes = {
    EmbeddedRedisBootstrapConfiguration.class,
    DriverLocationService.class
})
@ImportAutoConfiguration(classes = {EmbeddedRedisDependenciesAutoConfiguration.class,
    EmbeddedRedisBootstrapConfiguration.class})
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
    final Set<AvailableDriver> drivers = this.driverLocationService
        .findAvailableDrivers(BigDecimal.valueOf(0.0003), BigDecimal.valueOf(0.0004));
    assertThat(drivers).hasSize(2);
  }
}

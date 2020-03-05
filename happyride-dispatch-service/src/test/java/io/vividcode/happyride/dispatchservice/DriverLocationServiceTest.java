package io.vividcode.happyride.dispatchservice;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.playtika.test.redis.EmbeddedRedisBootstrapConfiguration;
import com.playtika.test.redis.EmbeddedRedisDependenciesAutoConfiguration;
import io.vividcode.happyride.dispatchservice.api.events.DriverLocation;
import java.math.BigDecimal;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.autoconfigure.data.redis.DataRedisTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;

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
@DisplayName("司机位置服务")
public class DriverLocationServiceTest {

  @Autowired
  DriverLocationService driverLocationService;

  @Test
  @DisplayName("查找可用司机")
  public void testFindAvailableDrivers() {
    driverLocationService.addAvailableDriver(
        new DriverLocation("driver1", "vehicle1", BigDecimal.ZERO, BigDecimal.ZERO));
    driverLocationService.addAvailableDriver(
        new DriverLocation("driver2", "vehicle2", BigDecimal.valueOf(0.0001),
            BigDecimal.valueOf(0.0002)));
    Set<AvailableDriver> drivers = driverLocationService
        .findAvailableDrivers(BigDecimal.valueOf(0.0003), BigDecimal.valueOf(0.0004));
    assertEquals(2, drivers.size());
  }
}

package io.vividcode.happyride.dispatchservice.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.playtika.test.redis.EmbeddedRedisBootstrapConfiguration;
import com.playtika.test.redis.EmbeddedRedisDependenciesAutoConfiguration;
import io.vividcode.happyride.dispatchservice.api.events.DriverLocation;
import java.math.BigDecimal;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.autoconfigure.data.redis.DataRedisTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;

@DataRedisTest
@EnableAutoConfiguration
@EnableScheduling
@ComponentScan
@ContextConfiguration(classes = {
    EmbeddedRedisBootstrapConfiguration.class
})
@ImportAutoConfiguration(classes = {EmbeddedRedisDependenciesAutoConfiguration.class, EmbeddedRedisBootstrapConfiguration.class})
@TestPropertySource(properties = {
    "embedded.redis.dockerImage=redis:5-alpine"
})
public class DispatcherServiceTest {

  @Autowired
  DispatcherService dispatcherService;

  @Test
  public void testFindAvailableDrivers() {
    dispatcherService.addAvailableDriver(
        new DriverLocation("driver1", "vehicle1", BigDecimal.ZERO, BigDecimal.ZERO));
    dispatcherService.addAvailableDriver(
        new DriverLocation("driver2", "vehicle2", BigDecimal.valueOf(0.01),
            BigDecimal.valueOf(0.02)));
    Set<AvailableDriver> drivers = dispatcherService.findAvailableDrivers(0.1, 0.1);
    assertEquals(2, drivers.size());
    System.out.println(drivers);
  }
}

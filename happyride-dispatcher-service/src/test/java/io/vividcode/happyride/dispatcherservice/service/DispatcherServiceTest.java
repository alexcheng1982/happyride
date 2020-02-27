package io.vividcode.happyride.dispatcherservice.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.playtika.test.redis.EmbeddedRedisBootstrapConfiguration;
import io.vividcode.happyride.dispatcherservice.api.events.DriverLocation;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@ContextConfiguration(classes = {TestApplication.class,
    EmbeddedRedisBootstrapConfiguration.class})
@TestPropertySource(properties = {
    "embedded.redis.dockerImage=redis:5-alpine"
})
public class DispatcherServiceTest {
  @Autowired
  DispatcherService dispatcherService;

  @Test
  public void testFindAvailableDrivers() {
    dispatcherService.addAvailableDriver(new DriverLocation("driver1", "vehicle1", BigDecimal.ZERO, BigDecimal.ZERO));
    dispatcherService.addAvailableDriver(new DriverLocation("driver2", "vehicle2", BigDecimal.valueOf(0.01), BigDecimal.valueOf(0.02)));
    List<AvailableDriver> drivers = dispatcherService.findAvailableDrivers(0.1, 0.1);
    assertEquals(2, drivers.size());
    System.out.println(drivers);
  }
}

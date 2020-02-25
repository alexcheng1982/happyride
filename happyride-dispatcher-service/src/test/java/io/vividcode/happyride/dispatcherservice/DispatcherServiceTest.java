package io.vividcode.happyride.dispatcherservice;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.playtika.test.redis.EmbeddedRedisBootstrapConfiguration;
import com.playtika.test.redis.EmbeddedRedisTestOperationsAutoConfiguration;
import io.vividcode.happyride.dispatcherservice.api.events.DriverLocation;
import io.vividcode.happyride.dispatcherservice.service.AvailableDriver;
import io.vividcode.happyride.dispatcherservice.service.DispatcherService;
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
    dispatcherService.addAvailableDriver(new DriverLocation("driver1", "vehicle1", 0, 0));
    dispatcherService.addAvailableDriver(new DriverLocation("driver2", "vehicle2", 0.01, 0.02));
    List<AvailableDriver> drivers = dispatcherService.findAvailableDrivers(0.1, 0.1, 100000);
    assertEquals(2, drivers.size());
    System.out.println(drivers);
  }
}

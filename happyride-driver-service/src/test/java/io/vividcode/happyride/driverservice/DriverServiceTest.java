package io.vividcode.happyride.driverservice;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.playtika.test.postgresql.EmbeddedPostgreSQLBootstrapConfiguration;
import com.playtika.test.postgresql.EmbeddedPostgreSQLDependenciesAutoConfiguration;
import io.vividcode.happyride.driverservice.api.web.CreateDriverRequest;
import io.vividcode.happyride.driverservice.model.Driver;
import io.vividcode.happyride.driverservice.service.DriverService;
import io.vividcode.happyride.postgres.common.EmbeddedPostgresConfiguration;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;

@DataJpaTest
@EnableAutoConfiguration
@AutoConfigureTestDatabase(replace = Replace.NONE)
@ComponentScan
@ContextConfiguration(classes = {
    EmbeddedPostgresConfiguration.class}
)
@ImportAutoConfiguration(classes = {
    EmbeddedPostgreSQLDependenciesAutoConfiguration.class,
    EmbeddedPostgreSQLBootstrapConfiguration.class
})
@TestPropertySource(properties = {
    "embedded.postgresql.docker-image=postgres:12-alpine"
})
@DisplayName("司机服务")
public class DriverServiceTest {

  @Autowired
  DriverService driverService;

  @Autowired
  DriverTestUtils testUtils;

  @Test
  @DisplayName("创建司机")
  public void testCreateDriver() {
    CreateDriverRequest request = testUtils.buildCreateDriverRequest(1);
    Driver driver = driverService.createDriver(request);
    assertNotNull(driver.getId());
    assertEquals(1, driver.getVehicles().size());
  }

  @Test
  @DisplayName("添加车辆到已有司机")
  public void testAddVehicle() {
    CreateDriverRequest request = testUtils.buildCreateDriverRequest(2);
    Driver driver = driverService.createDriver(request);
    driver = driverService.addVehicle(driver.getId(), testUtils.buildCreateVehicleRequest());
    assertEquals(3, driver.getVehicles().size());
  }
}

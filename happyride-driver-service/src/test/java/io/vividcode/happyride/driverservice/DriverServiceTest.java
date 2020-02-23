package io.vividcode.happyride.driverservice;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.playtika.test.postgresql.EmbeddedPostgreSQLBootstrapConfiguration;
import io.vividcode.happyride.driverservice.api.web.CreateDriverRequest;
import io.vividcode.happyride.driverservice.api.web.CreateVehicleRequest;
import io.vividcode.happyride.driverservice.model.Driver;
import io.vividcode.happyride.driverservice.service.DriverService;
import io.vividcode.happyride.postgres.common.EmbeddedPostgresConfiguration;
import java.util.Collections;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@ContextConfiguration(classes = {TestApplication.class,
    EmbeddedPostgreSQLBootstrapConfiguration.class, EmbeddedPostgresConfiguration.class})
@TestPropertySource(properties = {
    "embedded.postgresql.docker-image=postgres:12-alpine"
})
@DisplayName("Driver service")
public class DriverServiceTest {
  @Autowired
  DriverService driverService;

  @Test
  @DisplayName("Create driver")
  public void testCreateDriver() {
    CreateDriverRequest request = buildCreateDriverRequest();
    Driver driver = driverService.createDriver(request);
    assertNotNull(driver.getId());
    assertEquals(1, driver.getVehicles().size());
  }

  @Test
  @DisplayName("Add vehicle to an existing driver")
  public void testAddVehicle() {
    CreateDriverRequest request = buildCreateDriverRequest();
    Driver driver = driverService.createDriver(request);
    CreateVehicleRequest vehicleRequest = new CreateVehicleRequest();
    vehicleRequest.setMake("Make2");
    vehicleRequest.setMode("Mode2");
    vehicleRequest.setYear(2019);
    vehicleRequest.setRegistration("ABC");
    driver = driverService.addVehicle(driver.getId(), vehicleRequest);
    assertEquals(2, driver.getVehicles().size());
  }

  private CreateDriverRequest buildCreateDriverRequest() {
    CreateDriverRequest request = new CreateDriverRequest();
    request.setName("司机1");
    request.setEmail("d1@example.com");
    request.setMobilePhoneNumber("13800000000");
    CreateVehicleRequest vehicleRequest = new CreateVehicleRequest();
    vehicleRequest.setMake("Make1");
    vehicleRequest.setMode("Mode1");
    vehicleRequest.setYear(2010);
    vehicleRequest.setRegistration("XYZ");
    request.setVehicles(Collections.singleton(vehicleRequest));
    return request;
  }
}

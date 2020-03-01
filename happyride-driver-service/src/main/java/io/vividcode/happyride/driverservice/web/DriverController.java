package io.vividcode.happyride.driverservice.web;

import io.vividcode.happyride.driverservice.api.web.CreateDriverRequest;
import io.vividcode.happyride.driverservice.model.Driver;
import io.vividcode.happyride.driverservice.service.DriverService;
import java.net.URI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DriverController {

  @Autowired
  DriverService driverService;

  @GetMapping("{id}")
  public ResponseEntity<Driver> getDriver(@PathVariable("id") String driverId) {
    return driverService
        .getDriver(driverId).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
  }

  @PostMapping
  public ResponseEntity<Driver> createDriver(@RequestBody CreateDriverRequest request) {
    Driver driver = driverService.createDriver(request);
    return ResponseEntity.created(URI.create("/" + driver.getId())).body(driver);
  }
}

package io.vividcode.happyride.driverservice.web;

import io.vividcode.happyride.driverservice.api.web.CreateDriverRequest;
import io.vividcode.happyride.driverservice.api.web.DriverView;
import io.vividcode.happyride.driverservice.service.DriverService;
import java.net.URI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
public class DriverController {

  @Autowired
  DriverService driverService;

  @GetMapping("{id}")
  public ResponseEntity<DriverView> getDriver(@PathVariable("id") String driverId) {
    return driverService.getDriver(driverId)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }

  @PostMapping
  public ResponseEntity<Void> createDriver(@RequestBody CreateDriverRequest request) {
    DriverView driver = driverService.createDriver(request);
    return ResponseEntity.created(resourceCreated(driver.getId())).build();
  }

  private URI resourceCreated(String resourceId) {
    return ServletUriComponentsBuilder.fromCurrentRequest()
        .path("/{id}")
        .buildAndExpand(resourceId)
        .toUri();
  }
}

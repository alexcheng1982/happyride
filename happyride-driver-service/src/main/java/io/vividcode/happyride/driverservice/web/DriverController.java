package io.vividcode.happyride.driverservice.web;

import io.vividcode.happyride.driverservice.api.web.CreateDriverRequest;
import io.vividcode.happyride.driverservice.api.web.DriverView;
import io.vividcode.happyride.driverservice.api.web.VehicleView;
import io.vividcode.happyride.driverservice.model.Driver;
import io.vividcode.happyride.driverservice.model.Vehicle;
import io.vividcode.happyride.driverservice.service.DriverService;
import java.net.URI;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
public class DriverController {

  @Autowired
  DriverService driverService;

  @GetMapping("{id}")
  public ResponseEntity<DriverView> getDriver(@PathVariable("id") String driverId) {
    return driverService.getDriver(driverId)
        .map(this::createDriverView)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }

  @PostMapping
  public ResponseEntity<Void> createDriver(@RequestBody CreateDriverRequest request) {
    Driver driver = driverService.createDriver(request);
    return ResponseEntity.created(resourceCreated(driver.getId())).build();
  }

  private URI resourceCreated(String resourceId) {
    return ServletUriComponentsBuilder.fromCurrentRequest()
        .path("/{id}")
        .buildAndExpand(resourceId)
        .toUri();
  }

  private DriverView createDriverView(Driver driver) {
    DriverView view = new DriverView();
    view.setId(driver.getId());
    view.setName(driver.getName());
    view.setEmail(driver.getEmail());
    view.setMobilePhoneNumber(driver.getMobilePhoneNumber());
    view.setState(driver.getState().name());
    view.setVehicles(driver.getVehicles().stream()
        .map(this::createVehicleView).collect(Collectors.toList()));
    return view;
  }

  private VehicleView createVehicleView(Vehicle vehicle) {
    VehicleView view = new VehicleView();
    view.setId(vehicle.getId());
    view.setMake(vehicle.getMake());
    view.setMode(vehicle.getMode());
    view.setYear(vehicle.getYear());
    view.setRegistration(vehicle.getRegistration());
    return view;
  }
}

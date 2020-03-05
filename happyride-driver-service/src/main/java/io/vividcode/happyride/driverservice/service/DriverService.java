package io.vividcode.happyride.driverservice.service;

import io.vividcode.happyride.driverservice.api.web.CreateDriverRequest;
import io.vividcode.happyride.driverservice.api.web.CreateVehicleRequest;
import io.vividcode.happyride.driverservice.dataaccess.DriverRepository;
import io.vividcode.happyride.driverservice.model.Driver;
import io.vividcode.happyride.driverservice.model.Vehicle;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class DriverService {

  @Autowired
  DriverRepository driverRepository;

  public Optional<Driver> getDriver(String driverId) {
    return driverRepository.findById(driverId);
  }

  public Driver createDriver(CreateDriverRequest request) {
    Driver driver = new Driver();
    driver.setName(request.getName());
    driver.setEmail(request.getEmail());
    driver.setMobilePhoneNumber(request.getMobilePhoneNumber());
    List<Vehicle> vehicles = Optional.ofNullable(request.getVehicles()).map(
        requests -> requests.stream().map(this::createVehicle)
            .collect(Collectors.toList())).orElse(
        new ArrayList<>());
    driver.setVehicles(vehicles);
    driverRepository.save(driver);
    return driver;
  }

  public Driver addVehicle(String driverId, CreateVehicleRequest request) {
    Driver driver = driverRepository.findById(driverId)
        .orElseThrow(() -> new DriverNotFoundException(driverId));
    Vehicle vehicle = createVehicle(request);
    driver.addVehicle(vehicle);
    driverRepository.save(driver);
    return driver;
  }

  private Vehicle createVehicle(CreateVehicleRequest request) {
    Vehicle vehicle = new Vehicle();
    vehicle.setMake(request.getMake());
    vehicle.setMode(request.getMode());
    vehicle.setYear(request.getYear());
    vehicle.setRegistration(request.getRegistration());
    return vehicle;
  }
}

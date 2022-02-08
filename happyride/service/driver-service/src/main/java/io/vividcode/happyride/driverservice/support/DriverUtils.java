package io.vividcode.happyride.driverservice.support;

import io.vividcode.happyride.driverservice.api.web.DriverView;
import io.vividcode.happyride.driverservice.api.web.VehicleView;
import io.vividcode.happyride.driverservice.model.Driver;
import io.vividcode.happyride.driverservice.model.Vehicle;
import java.util.stream.Collectors;

public class DriverUtils {

  private DriverUtils() {
  }

  public static DriverView createDriverView(Driver driver) {
    DriverView view = new DriverView();
    view.setId(driver.getId());
    view.setName(driver.getName());
    view.setEmail(driver.getEmail());
    view.setMobilePhoneNumber(driver.getMobilePhoneNumber());
    view.setState(driver.getState().name());
    view.setVehicles(driver.getVehicles().stream()
        .map(DriverUtils::createVehicleView).collect(Collectors.toList()));
    return view;
  }

  public static VehicleView createVehicleView(Vehicle vehicle) {
    VehicleView view = new VehicleView();
    view.setId(vehicle.getId());
    view.setMake(vehicle.getMake());
    view.setMode(vehicle.getMode());
    view.setYear(vehicle.getYear());
    view.setRegistration(vehicle.getRegistration());
    return view;
  }
}

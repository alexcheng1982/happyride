package io.vividcode.happyride.driverservice.service;

import io.vividcode.happyride.driverservice.dataaccess.DriverRepository;
import io.vividcode.happyride.driverservice.model.Driver;
import java.util.Optional;
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
}

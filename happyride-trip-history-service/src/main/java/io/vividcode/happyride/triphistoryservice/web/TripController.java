package io.vividcode.happyride.triphistoryservice.web;

import io.vividcode.happyride.triphistoryservice.domain.Trip;
import io.vividcode.happyride.triphistoryservice.domain.TripService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TripController {

  @Autowired
  TripService tripService;

  @RequestMapping(value = "/passenger/{passengerId}", produces = MediaType.APPLICATION_JSON_VALUE)
  List<Trip> findByPassenger(
      @PathVariable("passengerId") final String passengerId) {
    return this.tripService.findByPassenger(passengerId);
  }

  @RequestMapping(value = "/driver/{driverId}", produces = MediaType.APPLICATION_JSON_VALUE)
  List<Trip> findByDriver(
      @PathVariable("driverId") final String driverId) {
    return this.tripService.findByDriver(driverId);
  }
}

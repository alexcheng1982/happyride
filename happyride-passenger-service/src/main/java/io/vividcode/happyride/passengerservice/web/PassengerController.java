package io.vividcode.happyride.passengerservice.web;

import io.vividcode.happyride.passengerservice.api.web.CreatePassengerRequest;
import io.vividcode.happyride.passengerservice.api.web.CreateUserAddressRequest;
import io.vividcode.happyride.passengerservice.domain.Passenger;
import io.vividcode.happyride.passengerservice.domain.UserAddress;
import io.vividcode.happyride.passengerservice.service.PassengerService;
import java.net.URI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PassengerController {

  @Autowired
  PassengerService passengerService;

  @GetMapping("{id}")
  public ResponseEntity<Passenger> getPassenger(@PathVariable("id") String passengerId) {
    return passengerService.getPassenger(passengerId).map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }

  @PostMapping
  public ResponseEntity<Passenger> createPassenger(@RequestBody CreatePassengerRequest request) {
    Passenger passenger = passengerService.createPassenger(request);
    return ResponseEntity.created(URI.create("/" + passenger.getId())).body(passenger);

  }

  @PostMapping("{id}/addresses")
  public ResponseEntity<UserAddress> createAddress(@PathVariable("id") String passengerId, @RequestBody CreateUserAddressRequest request) {
    UserAddress address = passengerService.addAddress(passengerId, request);
    return ResponseEntity.created(URI.create("/" + address.getId())).body(address);
  }

}

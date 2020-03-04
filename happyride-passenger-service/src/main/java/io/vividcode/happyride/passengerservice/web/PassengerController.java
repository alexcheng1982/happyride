package io.vividcode.happyride.passengerservice.web;

import io.vividcode.happyride.passengerservice.api.web.CreatePassengerRequest;
import io.vividcode.happyride.passengerservice.api.web.CreateUserAddressRequest;
import io.vividcode.happyride.passengerservice.api.web.PassengerView;
import io.vividcode.happyride.passengerservice.api.web.UserAddressView;
import io.vividcode.happyride.passengerservice.domain.Passenger;
import io.vividcode.happyride.passengerservice.domain.UserAddress;
import io.vividcode.happyride.passengerservice.service.PassengerService;
import java.net.URI;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
public class PassengerController {

  @Autowired
  PassengerService passengerService;

  @GetMapping("{id}")
  public ResponseEntity<PassengerView> getPassenger(@PathVariable("id") String passengerId) {
    return passengerService.getPassenger(passengerId)
        .map(this::createPassengerView)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }

  @PostMapping
  public ResponseEntity<PassengerView> createPassenger(
      @RequestBody CreatePassengerRequest request) {
    PassengerView passenger = createPassengerView(passengerService.createPassenger(request));
    return ResponseEntity.created(resourceCreated(passenger.getId())).body(passenger);
  }

  @PostMapping("{id}/addresses")
  public ResponseEntity<UserAddressView> createAddress(@PathVariable("id") String passengerId,
      @RequestBody CreateUserAddressRequest request) {
    UserAddressView address = createUserAddressView(
        passengerService.addAddress(passengerId, request));
    return ResponseEntity.created(resourceCreated(address.getId())).body(address);
  }

  @GetMapping("{passengerId}/addresses/{addressId}")
  public ResponseEntity<UserAddressView> getAddress(@PathVariable("passengerId") String passengerId,
      @PathVariable("addressId") String addressId) {
    return passengerService.getAddress(passengerId, addressId)
        .map(this::createUserAddressView)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }

  @DeleteMapping("{passengerId}/addresses/{addressId}")
  public ResponseEntity<Void> deleteAddress(@PathVariable("passengerId") String passengerId,
      @PathVariable("addressId") String addressId) {
    passengerService.deleteAddress(passengerId, addressId);
    return ResponseEntity.noContent().build();
  }

  private URI resourceCreated(String resourceId) {
    return ServletUriComponentsBuilder.fromCurrentRequest()
        .path("/{id}")
        .buildAndExpand(resourceId)
        .toUri();
  }

  private PassengerView createPassengerView(Passenger passenger) {
    return new PassengerView(passenger.getId(),
        passenger.getName(),
        passenger.getEmail(),
        passenger.getMobilePhoneNumber(),
        passenger.getUserAddresses().stream().map(this::createUserAddressView)
            .collect(Collectors.toList()));
  }

  private UserAddressView createUserAddressView(UserAddress userAddress) {
    return new UserAddressView(userAddress.getId(),
        userAddress.getPassenger().getId(),
        userAddress.getName(),
        userAddress.getAddressId());
  }
}

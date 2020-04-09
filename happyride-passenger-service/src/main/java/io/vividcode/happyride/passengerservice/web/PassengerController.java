package io.vividcode.happyride.passengerservice.web;

import io.vividcode.happyride.passengerservice.api.web.CreatePassengerRequest;
import io.vividcode.happyride.passengerservice.api.web.CreateUserAddressRequest;
import io.vividcode.happyride.passengerservice.api.web.PassengerView;
import io.vividcode.happyride.passengerservice.api.web.UserAddressView;
import io.vividcode.happyride.passengerservice.service.PassengerService;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/api/v1")
public class PassengerController {

  @Autowired
  PassengerService passengerService;

  @GetMapping
  public List<PassengerView> findAll() {
    return passengerService.findAll();
  }

  @GetMapping("{id}")
  public ResponseEntity<PassengerView> getPassenger(@PathVariable("id") String passengerId) {
    return passengerService.getPassenger(passengerId)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }

  @PostMapping
  public ResponseEntity<PassengerView> createPassenger(
      @RequestBody CreatePassengerRequest request) {
    PassengerView passenger = passengerService.createPassenger(request);
    return ResponseEntity.created(resourceCreated(passenger.getId())).body(passenger);
  }

  @GetMapping("{id}/addresses")
  public List<UserAddressView> getAddresses(@PathVariable("id") String passengerId) {
    return passengerService.getPassenger(passengerId)
        .map(PassengerView::getUserAddresses)
        .orElse(new ArrayList<>());
  }

  @PostMapping("{id}/addresses")
  public ResponseEntity<PassengerView> createAddress(@PathVariable("id") String passengerId,
      @RequestBody CreateUserAddressRequest request) {
    PassengerView passenger = passengerService.addAddress(passengerId, request);
    return ResponseEntity.ok(passenger);
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
}

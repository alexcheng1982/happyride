package io.vividcode.happyride.passengerservice.web;

import io.vividcode.happyride.passengerservice.api.web.CreatePassengerRequest;
import io.vividcode.happyride.passengerservice.api.web.CreateUserAddressRequest;
import io.vividcode.happyride.passengerservice.api.web.PassengerVO;
import io.vividcode.happyride.passengerservice.domain.PassengerService;
import java.net.URI;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.EntityLinks;
import org.springframework.hateoas.server.ExposesResourceFor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@ExposesResourceFor(PassengerVO.class)
@RequestMapping("/passenger")
public class PassengerController {

  @Autowired
  PassengerService passengerService;

  @Autowired
  EntityLinks entityLinks;

  @GetMapping
  public List<PassengerVO> findAll(
      @RequestParam(value = "page", required = false, defaultValue = "0") int page,
      @RequestParam(value = "size", required = false, defaultValue = "10") int size) {
    return this.passengerService.findAll(PageRequest.of(page, size));
  }

  @GetMapping("{id}")
  public ResponseEntity<EntityModel<PassengerVO>> getPassenger(
      @PathVariable("id") String passengerId) {
    return this.passengerService.getPassenger(passengerId)
        .map(passenger -> EntityModel
            .of(passenger,
                this.entityLinks
                    .linkToItemResource(PassengerVO.class, passengerId)
                    .withSelfRel()))
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }

  @PostMapping
  public ResponseEntity<PassengerVO> createPassenger(
      @RequestBody CreatePassengerRequest request) {
    PassengerVO passenger = this.passengerService
        .createPassenger(request);
    return ResponseEntity.created(this.resourceCreated(passenger.getId()))
        .body(passenger);
  }

  @PostMapping("{id}/addresses")
  public ResponseEntity<PassengerVO> createAddress(
      @PathVariable("id") String passengerId,
      @RequestBody CreateUserAddressRequest request) {
    PassengerVO passenger = this.passengerService
        .addAddress(passengerId, request);
    return ResponseEntity.ok(passenger);
  }

  @DeleteMapping("{passengerId}/addresses/{addressId}")
  public ResponseEntity<Void> deleteAddress(
      @PathVariable("passengerId") String passengerId,
      @PathVariable("addressId") String addressId) {
    this.passengerService.deleteAddress(passengerId, addressId);
    return ResponseEntity.noContent().build();
  }

  private URI resourceCreated(String resourceId) {
    return ServletUriComponentsBuilder.fromCurrentRequest()
        .path("/{id}")
        .buildAndExpand(resourceId)
        .toUri();
  }
}

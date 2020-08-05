package io.vividcode.happyride.tripservice.web;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.afford;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import io.vividcode.happyride.tripservice.api.events.CancellationParty;
import io.vividcode.happyride.tripservice.api.web.AcceptTripRequest;
import io.vividcode.happyride.tripservice.api.web.CreateTripRequest;
import io.vividcode.happyride.tripservice.api.web.TripVO;
import io.vividcode.happyride.tripservice.domain.TripService;
import java.net.URI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
public class TripController {

  @Autowired
  TripService tripService;

  @PostMapping
  public ResponseEntity<Void> createTrip(
      @RequestBody CreateTripRequest request) {
    TripVO trip = this.tripService
        .createTrip(request.getPassengerId(), request.getStartPos(),
            request.getEndPos());
    return ResponseEntity.created(this.resourceCreated(trip.getId())).build();
  }

  @GetMapping("{id}")
  public ResponseEntity<EntityModel<TripVO>> getTrip(
      @PathVariable("id") String id) {
    Link link = linkTo(methodOn(TripController.class).getTrip(id))
        .withSelfRel()
        .andAffordance(afford(methodOn(TripController.class).startTrip(id)))
        .andAffordance(
            afford(methodOn(TripController.class).cancelByPassenger(id)));
    return this.tripService.getTrip(id)
        .map(trip -> EntityModel.of(trip, link))
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }

  @PostMapping("{id}/accept")
  public void acceptTrip(@PathVariable("id") String id,
      @RequestBody AcceptTripRequest request) {
    this.tripService
        .driverAcceptTrip(id, request.getDriverId(), request.getPosLng(),
            request.getPosLat());
  }

  @PostMapping("{id}/cancelByPassenger")
  public ResponseEntity<?> cancelByPassenger(@PathVariable("id") String id) {
    this.tripService.shouldCancel(id, CancellationParty.PASSENGER);
    return ResponseEntity.noContent().build();
  }

  @PostMapping("{id}/cancelByDriver")
  public ResponseEntity<?> cancelByDriver(@PathVariable("id") String id) {
    this.tripService.shouldCancel(id, CancellationParty.DRIVER);
    return ResponseEntity.noContent().build();
  }

  @PostMapping("{id}/rejectCancellationByPassenger")
  public ResponseEntity<?> rejectCancellationByPassenger(
      @PathVariable("id") String id) {
    this.tripService.shouldNotCancel(id, CancellationParty.PASSENGER);
    return ResponseEntity.noContent().build();
  }

  @PostMapping("{id}/rejectCancellationByDriver")
  public ResponseEntity<?> rejectCancellationByDriver(
      @PathVariable("id") String id) {
    this.tripService.shouldNotCancel(id, CancellationParty.DRIVER);
    return ResponseEntity.noContent().build();
  }

  @PostMapping("{id}/start")
  public ResponseEntity<Void> startTrip(@PathVariable("id") String id) {
    this.tripService.markTripAsStarted(id);
    return ResponseEntity.noContent().build();
  }

  @PostMapping("{id}/finish")
  public ResponseEntity<Void> finishTrip(@PathVariable("id") String id) {
    this.tripService.markTripAsFinished(id);
    return ResponseEntity.noContent().build();
  }

  private URI resourceCreated(String resourceId) {
    return ServletUriComponentsBuilder.fromCurrentRequest()
        .path("/{id}")
        .buildAndExpand(resourceId)
        .toUri();
  }
}

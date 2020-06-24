package io.vividcode.happyride.tripservice.web;

import io.vividcode.happyride.tripservice.api.events.CancellationParty;
import io.vividcode.happyride.tripservice.api.web.AcceptTripRequest;
import io.vividcode.happyride.tripservice.api.web.CreateTripRequest;
import io.vividcode.happyride.tripservice.api.web.TripVO;
import io.vividcode.happyride.tripservice.domain.TripService;
import java.net.URI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
public class TripController {

  @Autowired
  TripService tripService;

  @PostMapping
  public ResponseEntity<Void> createTrip(@RequestBody CreateTripRequest request) {
    TripVO trip = tripService
        .createTrip(request.getPassengerId(), request.getStartPos(), request.getEndPos());
    return ResponseEntity.created(resourceCreated(trip.getId())).build();
  }

  @GetMapping("{id}")
  public ResponseEntity<TripVO> getTrip(@PathVariable("id") String id) {
    return tripService.getTrip(id)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }

  @PostMapping("{id}/accept")
  public void acceptTrip(@PathVariable("id") String id,
      @RequestBody AcceptTripRequest request) {
    tripService
        .driverAcceptTrip(id, request.getDriverId(), request.getPosLng(), request.getPosLat());
  }

  @PostMapping("{id}/cancelByPassenger")
  public void cancelByPassenger(@PathVariable("id") String id) {
    tripService.shouldCancel(id, CancellationParty.PASSENGER);
  }

  @PostMapping("{id}/cancelByDriver")
  public void cancelByDriver(@PathVariable("id") String id) {
    tripService.shouldCancel(id, CancellationParty.DRIVER);
  }

  @PostMapping("{id}/rejectCancellationByPassenger")
  public void rejectCancellationByPassenger(@PathVariable("id") String id) {
    tripService.shouldNotCancel(id, CancellationParty.PASSENGER);
  }

  @PostMapping("{id}/rejectCancellationByDriver")
  public void rejectCancellationByDriver(@PathVariable("id") String id) {
    tripService.shouldNotCancel(id, CancellationParty.DRIVER);
  }

  @PostMapping("{id}/start")
  public ResponseEntity<Void> startTrip(@PathVariable("id") String id) {
    tripService.markTripAsStarted(id);
    return ResponseEntity.noContent().build();
  }

  @PostMapping("{id}/finish")
  public ResponseEntity<Void> finishTrip(@PathVariable("id") String id) {
    tripService.markTripAsFinished(id);
    return ResponseEntity.noContent().build();
  }

  private URI resourceCreated(String resourceId) {
    return ServletUriComponentsBuilder.fromCurrentRequest()
        .path("/{id}")
        .buildAndExpand(resourceId)
        .toUri();
  }
}

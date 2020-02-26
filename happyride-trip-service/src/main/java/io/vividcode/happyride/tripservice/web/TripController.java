package io.vividcode.happyride.tripservice.web;

import io.vividcode.happyride.tripservice.api.web.AcceptTripRequest;
import io.vividcode.happyride.tripservice.api.web.CreateTripRequest;
import io.vividcode.happyride.tripservice.domain.Trip;
import io.vividcode.happyride.tripservice.service.TripService;
import java.net.URI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TripController {

  @Autowired
  TripService tripService;

  @PostMapping
  public ResponseEntity<Void> createTrip(@RequestBody CreateTripRequest request) {
    Trip created = tripService.createTrip(request.getPassengerId(), request.getStartPos(), request.getEndPos());
    return ResponseEntity.created(URI.create("/" + created.getId())).build();
  }

  @GetMapping("{id}")
  public ResponseEntity<Trip> getRide(@PathVariable("id") String id) {
    return tripService.getRide(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
  }

  @PostMapping("/accept")
  public ResponseEntity<Void> acceptTrip(@RequestBody AcceptTripRequest request) {
    tripService.acceptTrip(request.getTripId(), request.getDriverId());
    return ResponseEntity.noContent().build();
  }
}

package io.vividcode.happyride.tripservice.cqrs.web;

import io.vividcode.happyride.tripservice.api.web.CreateTripRequest;
import io.vividcode.happyride.tripservice.cqrs.TripService;
import java.util.concurrent.CompletableFuture;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TripController {
  @Autowired
  TripService tripService;

  @PostMapping
  public CompletableFuture<Void> createTrip(@RequestBody CreateTripRequest request) {
    return tripService
        .createTrip(request.getPassengerId(), request.getStartPos(), request.getEndPos());
  }

}

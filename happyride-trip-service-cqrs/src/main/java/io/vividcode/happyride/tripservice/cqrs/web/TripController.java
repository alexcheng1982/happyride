package io.vividcode.happyride.tripservice.cqrs.web;

import io.vividcode.happyride.tripservice.api.web.CreateTripRequest;
import io.vividcode.happyride.tripservice.cqrs.api.FetchTripQuery;
import io.vividcode.happyride.tripservice.cqrs.api.TripSummary;
import io.vividcode.happyride.tripservice.cqrs.domain.TripService;
import java.net.URI;
import java.util.concurrent.CompletableFuture;
import org.axonframework.queryhandling.QueryGateway;
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

  @Autowired
  QueryGateway queryGateway;

  @PostMapping
  public CompletableFuture<ResponseEntity<Void>> createTrip(
      @RequestBody CreateTripRequest request) {
    return tripService
        .createTrip(request.getPassengerId(), request.getStartPos(), request.getEndPos())
        .thenApply(tripId -> ResponseEntity.created(URI.create("/" + tripId)).build());
  }

  @PostMapping("{id}/cancel")
  public CompletableFuture<Void> cancelTrip(@PathVariable("id") String tripId) {
    return tripService.cancelTrip(tripId);
  }

  @PostMapping("{id}/confirm")
  public CompletableFuture<Void> confirmTrip(@PathVariable("id") String tripId) {
    return tripService.confirmTrip(tripId);
  }

  @GetMapping("{id}")
  public CompletableFuture<TripSummary> getTrip(@PathVariable("id") String tripId) {
    return queryGateway.query(new FetchTripQuery(tripId), TripSummary.class);
  }
}

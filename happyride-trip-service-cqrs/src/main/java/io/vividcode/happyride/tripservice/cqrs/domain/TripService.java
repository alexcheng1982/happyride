package io.vividcode.happyride.tripservice.cqrs.domain;

import io.vividcode.happyride.common.PositionView;
import io.vividcode.happyride.tripservice.api.events.TripDetails;
import io.vividcode.happyride.tripservice.cqrs.api.CancelTripCommand;
import io.vividcode.happyride.tripservice.cqrs.api.ConfirmTripCommand;
import io.vividcode.happyride.tripservice.cqrs.api.CreateTripCommand;
import io.vividcode.happyride.tripservice.cqrs.api.FetchTripQuery;
import io.vividcode.happyride.tripservice.cqrs.api.TripSummary;
import io.vividcode.happyride.tripservice.cqrs.dataaccess.TripViewRepository;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TripService {

  @Autowired
  CommandGateway commandGateway;

  @Autowired
  TripViewRepository tripViewRepository;

  public CompletableFuture<String> createTrip(String passengerId, PositionView startPos,
      PositionView endPos) {
    String tripId = UUID.randomUUID().toString();
    TripDetails tripDetails = new TripDetails(passengerId, startPos, endPos);
    CreateTripCommand command = new CreateTripCommand(tripId, tripDetails);
    return commandGateway.send(command);
  }

  public CompletableFuture<Void> cancelTrip(String tripId) {
    CancelTripCommand command = new CancelTripCommand(tripId);
    return commandGateway.send(command);
  }

  public CompletableFuture<Void> confirmTrip(String tripId) {
    ConfirmTripCommand command = new ConfirmTripCommand(tripId);
    return commandGateway.send(command);
  }

  @QueryHandler
  public TripSummary handle(FetchTripQuery query) {
    return tripViewRepository.findById(query.getTripId())
        .map(tripView -> {
          TripSummary tripSummary = new TripSummary();
          tripSummary.setId(tripView.getId());
          tripSummary
              .setStartPos(new PositionView(tripView.getStartPosLng(), tripView.getStartPosLat()));
          tripSummary.setEndPos(new PositionView(tripView.getEndPosLng(), tripView.getEndPosLat()));
          tripSummary.setState(tripView.getState());
          return tripSummary;
        })
        .orElseThrow(() -> new TripNotFoundException(query.getTripId()));
  }
}

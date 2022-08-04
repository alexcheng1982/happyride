package io.vividcode.happyride.tripservice.cqrs.domain;

import io.vividcode.happyride.common.PositionVO;
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

  public CompletableFuture<String> createTrip(final String passengerId,
      final PositionVO startPos,
      final PositionVO endPos) {
    final String tripId = UUID.randomUUID().toString();
    final TripDetails tripDetails = new TripDetails(passengerId, startPos,
        endPos);
    final CreateTripCommand command = new CreateTripCommand(tripId,
        tripDetails);
    return this.commandGateway.send(command);
  }

  public CompletableFuture<Void> cancelTrip(final String tripId) {
    final CancelTripCommand command = new CancelTripCommand(tripId);
    return this.commandGateway.send(command);
  }

  public CompletableFuture<Void> confirmTrip(final String tripId) {
    final ConfirmTripCommand command = new ConfirmTripCommand(tripId);
    return this.commandGateway.send(command);
  }

  @QueryHandler
  public TripSummary queryTrip(final FetchTripQuery query) {
    return this.tripViewRepository.findById(query.getTripId())
        .map(tripView -> {
          final TripSummary tripSummary = new TripSummary();
          tripSummary.setId(tripView.getId());
          tripSummary
              .setStartPos(new PositionVO(tripView.getStartPosLng(),
                  tripView.getStartPosLat()));
          tripSummary.setEndPos(
              new PositionVO(tripView.getEndPosLng(), tripView.getEndPosLat()));
          tripSummary.setState(tripView.getState());
          return tripSummary;
        })
        .orElseThrow(() -> new TripNotFoundException(query.getTripId()));
  }
}

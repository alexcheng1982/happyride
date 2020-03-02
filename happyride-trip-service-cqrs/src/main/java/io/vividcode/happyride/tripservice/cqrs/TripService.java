package io.vividcode.happyride.tripservice.cqrs;

import io.vividcode.happyride.common.Position;
import io.vividcode.happyride.tripservice.api.events.TripDetails;
import io.vividcode.happyride.tripservice.cqrs.api.CreateTripCommand;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TripService {
  @Autowired
  CommandGateway commandGateway;

  public CompletableFuture<Void> createTrip(String passengerId, Position startPos, Position endPos) {
    String tripId = UUID.randomUUID().toString();
    TripDetails tripDetails = new TripDetails(passengerId, startPos, endPos);
    CreateTripCommand command = new CreateTripCommand(tripId, tripDetails);
    return commandGateway.send(command);
  }
}

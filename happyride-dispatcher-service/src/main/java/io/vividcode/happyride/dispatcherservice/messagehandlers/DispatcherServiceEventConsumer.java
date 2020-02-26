package io.vividcode.happyride.dispatcherservice.messagehandlers;

import com.google.common.collect.Lists;
import io.eventuate.tram.events.subscriber.DomainEventEnvelope;
import io.eventuate.tram.events.subscriber.DomainEventHandlers;
import io.eventuate.tram.events.subscriber.DomainEventHandlersBuilder;
import io.vividcode.happyride.common.Position;
import io.vividcode.happyride.dispatcherservice.service.AvailableDriver;
import io.vividcode.happyride.dispatcherservice.service.DispatcherService;
import io.vividcode.happyride.tripservice.api.events.AcceptTripEvent;
import io.vividcode.happyride.tripservice.api.events.TripCreatedEvent;
import io.vividcode.happyride.tripservice.api.events.TripDetails;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;

public class DispatcherServiceEventConsumer {
  @Autowired
  DispatcherService dispatcherService;

  public DomainEventHandlers domainEventHandlers() {
    return DomainEventHandlersBuilder
        .forAggregateType("io.vividcode.happyride.tripservice.domain.Trip")
        .onEvent(TripCreatedEvent.class, this::onTripCreated)
        .onEvent(AcceptTripEvent.class, this::onAcceptTrip)
        .build();
  }

  private void onTripCreated(DomainEventEnvelope<TripCreatedEvent> envelope) {
    TripDetails tripDetails = envelope.getEvent().getTripDetails();
    Position startPos = tripDetails.getStartPos();
    List<AvailableDriver> availableDrivers = dispatcherService
        .findAvailableDrivers(startPos.getLng().doubleValue(), startPos.getLat().doubleValue(),
            100000);
    Set<String> drivers = availableDrivers.stream().map(AvailableDriver::getDriverId).collect(
        Collectors.toSet());
    dispatcherService.dispatchTrip(envelope.getAggregateId(), tripDetails, drivers);
  }

  private void onAcceptTrip(DomainEventEnvelope<AcceptTripEvent> envelope) {

  }
}

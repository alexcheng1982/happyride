package io.vividcode.happyride.dispatcherservice.messagehandlers;

import io.eventuate.tram.events.subscriber.DomainEventEnvelope;
import io.eventuate.tram.events.subscriber.DomainEventHandlers;
import io.eventuate.tram.events.subscriber.DomainEventHandlersBuilder;
import io.vividcode.happyride.common.Position;
import io.vividcode.happyride.dispatcherservice.service.AvailableDriver;
import io.vividcode.happyride.dispatcherservice.service.DispatcherService;
import io.vividcode.happyride.tripservice.api.events.TripCreatedEvent;
import io.vividcode.happyride.tripservice.api.events.TripDetails;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;

public class DispatcherServiceEventConsumer {
  @Autowired
  DispatcherService dispatcherService;

  public DomainEventHandlers domainEventHandlers() {
    return DomainEventHandlersBuilder
        .forAggregateType("io.vividcode.happyride.tripservice.domain.Trip")
        .onEvent(TripCreatedEvent.class, this::onTripCreated)
        .build();
  }

  private void onTripCreated(DomainEventEnvelope<TripCreatedEvent> envelope) {
    TripDetails tripDetails = envelope.getEvent().getTripDetails();
    Position startPos = tripDetails.getStartPos();
    List<AvailableDriver> availableDrivers = dispatcherService
        .findAvailableDrivers(startPos.getLng().doubleValue(), startPos.getLat().doubleValue(),
            100000);
    System.out.println(availableDrivers);
  }
}

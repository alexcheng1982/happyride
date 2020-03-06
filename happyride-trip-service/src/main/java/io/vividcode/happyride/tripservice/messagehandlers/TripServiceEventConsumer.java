package io.vividcode.happyride.tripservice.messagehandlers;

import io.eventuate.tram.events.subscriber.DomainEventEnvelope;
import io.eventuate.tram.events.subscriber.DomainEventHandlers;
import io.eventuate.tram.events.subscriber.DomainEventHandlersBuilder;
import io.vividcode.happyride.dispatchservice.api.events.TripAcceptanceSelectedEvent;
import io.vividcode.happyride.dispatchservice.api.events.TripDispatchedEvent;
import io.vividcode.happyride.tripservice.service.TripService;
import org.springframework.beans.factory.annotation.Autowired;

public class TripServiceEventConsumer {

  @Autowired
  TripService tripService;

  public DomainEventHandlers domainEventHandlers() {
    return DomainEventHandlersBuilder
        .forAggregateType("io.vividcode.happyride.dispatcherservice.domain.Dispatch")
        .onEvent(TripDispatchedEvent.class, this::onTripDispatched)
        .onEvent(TripAcceptanceSelectedEvent.class, this::onTripAccepted)
        .build();
  }

  private void onTripDispatched(DomainEventEnvelope<TripDispatchedEvent> envelope) {
    tripService.markTripAsDispatched(envelope.getEvent().getTripId());
  }

  private void onTripAccepted(DomainEventEnvelope<TripAcceptanceSelectedEvent> envelope) {
    TripAcceptanceSelectedEvent event = envelope.getEvent();
    tripService.markTripAsAccepted(event.getTripId(), event.getDriverId());
  }
}

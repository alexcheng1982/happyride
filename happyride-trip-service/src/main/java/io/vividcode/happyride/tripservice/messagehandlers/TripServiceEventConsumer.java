package io.vividcode.happyride.tripservice.messagehandlers;

import io.eventuate.tram.events.subscriber.DomainEventEnvelope;
import io.eventuate.tram.events.subscriber.DomainEventHandlers;
import io.eventuate.tram.events.subscriber.DomainEventHandlersBuilder;
import io.vividcode.happyride.dispatchservice.api.events.TripAcceptanceSelectedEvent;
import io.vividcode.happyride.dispatchservice.api.events.TripDispatchFailedEvent;
import io.vividcode.happyride.dispatchservice.api.events.TripDispatchedEvent;
import io.vividcode.happyride.tripservice.domain.TripService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
public class TripServiceEventConsumer {

  @Autowired
  TripService tripService;

  public DomainEventHandlers domainEventHandlers() {
    return DomainEventHandlersBuilder
        .forAggregateType("io.vividcode.happyride.dispatchservice.domain.Dispatch")
        .onEvent(TripDispatchedEvent.class, this::onTripDispatched)
        .onEvent(TripAcceptanceSelectedEvent.class, this::onTripAccepted)
        .onEvent(TripDispatchFailedEvent.class, this::onTripDispatchFailed)
        .build();
  }


  private void onTripDispatched(DomainEventEnvelope<TripDispatchedEvent> envelope) {
    tripService.markTripAsDispatched(envelope.getEvent().getTripId());
  }

  private void onTripAccepted(DomainEventEnvelope<TripAcceptanceSelectedEvent> envelope) {
    TripAcceptanceSelectedEvent event = envelope.getEvent();
    tripService.markTripAsAccepted(event.getTripId(), event.getDriverId());
  }

  private void onTripDispatchFailed(DomainEventEnvelope<TripDispatchFailedEvent> envelope) {
    tripService.markTripAsFailed(envelope.getEvent().getTripId());
  }
}

package io.vividcode.happyride.dispatcherservice.messagehandlers;

import io.eventuate.tram.events.subscriber.DomainEventEnvelope;
import io.eventuate.tram.events.subscriber.DomainEventHandlers;
import io.eventuate.tram.events.subscriber.DomainEventHandlersBuilder;
import io.vividcode.happyride.dispatcherservice.service.DispatcherService;
import io.vividcode.happyride.tripservice.api.events.DriverAcceptTripEvent;
import io.vividcode.happyride.tripservice.api.events.TripCreatedEvent;
import io.vividcode.happyride.tripservice.api.events.TripDetails;
import org.springframework.beans.factory.annotation.Autowired;

public class DispatcherServiceEventConsumer {
  @Autowired
  DispatcherService dispatcherService;

  public DomainEventHandlers domainEventHandlers() {
    return DomainEventHandlersBuilder
        .forAggregateType("io.vividcode.happyride.tripservice.domain.Trip")
        .onEvent(TripCreatedEvent.class, this::onTripCreated)
        .onEvent(DriverAcceptTripEvent.class, this::onDriverAcceptTrip)
        .build();
  }

  private void onTripCreated(DomainEventEnvelope<TripCreatedEvent> envelope) {
    TripDetails tripDetails = envelope.getEvent().getTripDetails();
    dispatcherService.dispatchTrip(envelope.getAggregateId(), tripDetails);
  }

  private void onDriverAcceptTrip(DomainEventEnvelope<DriverAcceptTripEvent> envelope) {
    dispatcherService.submitTripAcceptance(envelope.getAggregateId(), envelope.getEvent().getAcceptTripDetails());
  }
}

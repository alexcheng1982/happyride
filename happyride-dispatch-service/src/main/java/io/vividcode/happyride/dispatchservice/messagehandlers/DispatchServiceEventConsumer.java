package io.vividcode.happyride.dispatchservice.messagehandlers;

import io.eventuate.tram.events.subscriber.DomainEventEnvelope;
import io.eventuate.tram.events.subscriber.DomainEventHandlers;
import io.eventuate.tram.events.subscriber.DomainEventHandlersBuilder;
import io.vividcode.happyride.dispatchservice.DispatchService;
import io.vividcode.happyride.tripservice.api.events.DriverAcceptTripEvent;
import io.vividcode.happyride.tripservice.api.events.TripCreatedEvent;
import io.vividcode.happyride.tripservice.api.events.TripDetails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class DispatchServiceEventConsumer {

  @Autowired
  DispatchService dispatchService;

  private static final Logger LOGGER = LoggerFactory
      .getLogger(DispatchServiceEventConsumer.class);

  public DomainEventHandlers domainEventHandlers() {
    return DomainEventHandlersBuilder
        .forAggregateType("io.vividcode.happyride.tripservice.domain.Trip")
        .onEvent(TripCreatedEvent.class, this::onTripCreated)
        .onEvent(DriverAcceptTripEvent.class, this::onDriverAcceptTrip)
        .build();
  }

  private void onTripCreated(DomainEventEnvelope<TripCreatedEvent> envelope) {
    TripDetails tripDetails = envelope.getEvent().getTripDetails();
    try {
      dispatchService.dispatchTrip(envelope.getAggregateId(), tripDetails);
    } catch (Exception e) {
      LOGGER.warn("Failed to dispatch trip {}", envelope.getAggregateId(), e);
    }
  }

  private void onDriverAcceptTrip(DomainEventEnvelope<DriverAcceptTripEvent> envelope) {
    dispatchService.submitTripAcceptance(envelope.getAggregateId(),
        envelope.getEvent().getAcceptTripDetails());
  }
}

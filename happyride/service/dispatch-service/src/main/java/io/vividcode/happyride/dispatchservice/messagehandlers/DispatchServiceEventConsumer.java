package io.vividcode.happyride.dispatchservice.messagehandlers;

import io.eventuate.tram.events.subscriber.DomainEventEnvelope;
import io.eventuate.tram.events.subscriber.DomainEventHandlers;
import io.eventuate.tram.events.subscriber.DomainEventHandlersBuilder;
import io.vividcode.happyride.dispatchservice.DispatchService;
import io.vividcode.happyride.tripservice.api.events.DriverAcceptTripEvent;
import io.vividcode.happyride.tripservice.api.events.TripCancelledEvent;
import io.vividcode.happyride.tripservice.api.events.TripConfirmedEvent;
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
        .onEvent(TripConfirmedEvent.class, this::onTripConfirmed)
        .onEvent(DriverAcceptTripEvent.class, this::onDriverAcceptTrip)
        .onEvent(TripCancelledEvent.class, this::onTripCancelled)
        .build();
  }

  private void onTripConfirmed(
      final DomainEventEnvelope<TripConfirmedEvent> envelope) {
    final TripDetails tripDetails = envelope.getEvent().getTripDetails();
    try {
      this.dispatchService.dispatchTrip(envelope.getAggregateId(), tripDetails);
    } catch (final Exception e) {
      LOGGER.warn("Failed to dispatch trip {}", envelope.getAggregateId(), e);
    }
  }

  private void onDriverAcceptTrip(
      final DomainEventEnvelope<DriverAcceptTripEvent> envelope) {
    this.dispatchService.submitTripAcceptance(envelope.getAggregateId(),
        envelope.getEvent().getAcceptTripDetails());
  }

  private void onTripCancelled(
      final DomainEventEnvelope<TripCancelledEvent> envelope) {
    this.dispatchService.cancelDispatch(envelope.getAggregateId());
  }
}

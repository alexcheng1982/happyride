package io.vividcode.happyride.triphistoryservice.messagehandlers;

import io.eventuate.tram.events.subscriber.DomainEventEnvelope;
import io.eventuate.tram.events.subscriber.DomainEventHandlers;
import io.eventuate.tram.events.subscriber.DomainEventHandlersBuilder;
import io.vividcode.happyride.dispatchservice.api.events.TripAcceptanceSelectedEvent;
import io.vividcode.happyride.passengerservice.api.events.PassengerDetailsUpdatedEvent;
import io.vividcode.happyride.triphistoryservice.domain.TripService;
import io.vividcode.happyride.tripservice.api.TripState;
import io.vividcode.happyride.tripservice.api.events.TripCancelledEvent;
import io.vividcode.happyride.tripservice.api.events.TripConfirmedEvent;
import io.vividcode.happyride.tripservice.api.events.TripCreatedEvent;
import org.springframework.beans.factory.annotation.Autowired;

public class TripHistoryServiceEventConsumer {

  @Autowired
  TripService tripService;

  public DomainEventHandlers tripDomainEventHandlers() {
    return DomainEventHandlersBuilder
        .forAggregateType(
            "io.vividcode.happyride.tripservice.domain.Trip")
        .onEvent(TripCreatedEvent.class, this::onTripCreated)
        .onEvent(TripConfirmedEvent.class, this::onTripConfirmed)
        .onEvent(TripCancelledEvent.class, this::onTripCancelled)
        .build();
  }

  public DomainEventHandlers passengerDomainEventHandlers() {
    return DomainEventHandlersBuilder
        .forAggregateType(
            "io.vividcode.happyride.passengerservice.domain.Passenger")
        .onEvent(PassengerDetailsUpdatedEvent.class,
            this::onPassengerDetailsUpdated)
        .build();
  }

  public DomainEventHandlers dispatchDomainEventHandlers() {
    return DomainEventHandlersBuilder
        .forAggregateType(
            "io.vividcode.happyride.dispatchservice.domain.Dispatch")
        .onEvent(TripAcceptanceSelectedEvent.class, this::onTripAccepted)
        .build();
  }

  private void onTripCreated(
      final DomainEventEnvelope<TripCreatedEvent> envelope) {
    this.tripService.createTrip(envelope.getAggregateId(),
        envelope.getEvent().getTripDetails());
  }

  private void onPassengerDetailsUpdated(
      final DomainEventEnvelope<PassengerDetailsUpdatedEvent> envelope) {
    this.tripService
        .updatePassengerDetails(envelope.getAggregateId(),
            envelope.getEvent().getPassengerDetails());
  }

  private void onTripConfirmed(
      final DomainEventEnvelope<TripConfirmedEvent> envelope) {
    this.tripService
        .updateTripState(envelope.getAggregateId(), TripState.CONFIRMED);
  }

  private void onTripCancelled(
      final DomainEventEnvelope<TripCancelledEvent> envelope) {
    this.tripService
        .updateTripState(envelope.getAggregateId(), TripState.CANCELLED);
  }

  private void onTripAccepted(
      final DomainEventEnvelope<TripAcceptanceSelectedEvent> envelope) {
    final TripAcceptanceSelectedEvent event = envelope.getEvent();
    this.tripService
        .setTripDriver(envelope.getAggregateId(), event.getDriverId());
  }
}

package io.vividcode.happyride.passengerservice.messagehandlers;

import io.eventuate.tram.events.publisher.DomainEventPublisher;
import io.eventuate.tram.events.subscriber.DomainEventEnvelope;
import io.eventuate.tram.events.subscriber.DomainEventHandlers;
import io.eventuate.tram.events.subscriber.DomainEventHandlersBuilder;
import io.vividcode.happyride.passengerservice.api.events.PassengerDetails;
import io.vividcode.happyride.passengerservice.api.events.PassengerDetailsUpdatedEvent;
import io.vividcode.happyride.passengerservice.domain.PassengerService;
import io.vividcode.happyride.tripservice.api.events.TripCreatedEvent;
import java.util.Collections;
import org.springframework.beans.factory.annotation.Autowired;

public class PassengerServiceEventConsumer {

  @Autowired
  PassengerService passengerService;

  @Autowired
  DomainEventPublisher domainEventPublisher;

  public DomainEventHandlers domainEventHandlers() {
    return DomainEventHandlersBuilder
        .forAggregateType("io.vividcode.happyride.tripservice.domain.Trip")
        .onEvent(TripCreatedEvent.class, this::onTripCreated)
        .build();
  }

  private void onTripCreated(
      final DomainEventEnvelope<TripCreatedEvent> envelope) {
    final String passengerId = envelope.getEvent().getTripDetails()
        .getPassengerId();
    this.passengerService.getPassenger(passengerId)
        .ifPresent(passenger -> {
          final PassengerDetails passengerDetails = new PassengerDetails(
              passenger.getName());
          this.domainEventPublisher
              .publish(
                  "io.vividcode.happyride.passengerservice.domain.Passenger",
                  passengerId,
                  Collections.singletonList(
                      new PassengerDetailsUpdatedEvent(passengerDetails)));
        });
  }
}

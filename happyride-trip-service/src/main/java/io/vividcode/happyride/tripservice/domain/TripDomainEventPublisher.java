package io.vividcode.happyride.tripservice.domain;

import io.eventuate.tram.events.aggregates.AbstractAggregateDomainEventPublisher;
import io.eventuate.tram.events.publisher.DomainEventPublisher;
import io.vividcode.happyride.tripservice.api.events.TripDomainEvent;

public class TripDomainEventPublisher extends
    AbstractAggregateDomainEventPublisher<Trip, TripDomainEvent> {

  public TripDomainEventPublisher(
      DomainEventPublisher eventPublisher) {
    super(eventPublisher, Trip.class, Trip::getId);
  }
}

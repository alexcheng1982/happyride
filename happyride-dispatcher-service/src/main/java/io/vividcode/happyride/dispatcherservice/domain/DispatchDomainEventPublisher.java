package io.vividcode.happyride.dispatcherservice.domain;

import io.eventuate.tram.events.aggregates.AbstractAggregateDomainEventPublisher;
import io.eventuate.tram.events.publisher.DomainEventPublisher;
import io.vividcode.happyride.dispatcherservice.api.events.DispatchDomainEvent;

public class DispatchDomainEventPublisher extends
    AbstractAggregateDomainEventPublisher<Dispatch, DispatchDomainEvent> {
  public DispatchDomainEventPublisher(DomainEventPublisher eventPublisher) {
    super(eventPublisher, Dispatch.class, Dispatch::getId);
  }
}

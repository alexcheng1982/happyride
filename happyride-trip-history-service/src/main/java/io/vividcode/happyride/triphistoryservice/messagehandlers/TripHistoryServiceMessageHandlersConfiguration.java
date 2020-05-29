package io.vividcode.happyride.triphistoryservice.messagehandlers;

import io.eventuate.tram.events.subscriber.DomainEventDispatcher;
import io.eventuate.tram.events.subscriber.DomainEventDispatcherFactory;
import io.eventuate.tram.spring.events.subscriber.TramEventSubscriberConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(TramEventSubscriberConfiguration.class)
public class TripHistoryServiceMessageHandlersConfiguration {

  @Bean
  public TripHistoryServiceEventConsumer tripHistoryServiceEventConsumer() {
    return new TripHistoryServiceEventConsumer();
  }

  @Bean
  public DomainEventDispatcher tripDomainEventDispatcher(
      final TripHistoryServiceEventConsumer tripHistoryServiceEventConsumer,
      final DomainEventDispatcherFactory domainEventDispatcherFactory) {
    return domainEventDispatcherFactory
        .make("tripHistoryServiceTripEvents",
            tripHistoryServiceEventConsumer.tripDomainEventHandlers());
  }

  @Bean
  public DomainEventDispatcher passengerDomainEventDispatcher(
      final TripHistoryServiceEventConsumer tripHistoryServiceEventConsumer,
      final DomainEventDispatcherFactory domainEventDispatcherFactory) {
    return domainEventDispatcherFactory
        .make("tripHistoryServicePassengerEvents",
            tripHistoryServiceEventConsumer.passengerDomainEventHandlers());
  }

  @Bean
  public DomainEventDispatcher dispatchDomainEventDispatcher(
      final TripHistoryServiceEventConsumer tripHistoryServiceEventConsumer,
      final DomainEventDispatcherFactory domainEventDispatcherFactory) {
    return domainEventDispatcherFactory
        .make("tripHistoryServiceDispatchEvents",
            tripHistoryServiceEventConsumer.dispatchDomainEventHandlers());
  }
}

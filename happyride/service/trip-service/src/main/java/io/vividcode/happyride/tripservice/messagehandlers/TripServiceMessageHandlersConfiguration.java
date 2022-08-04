package io.vividcode.happyride.tripservice.messagehandlers;

import io.eventuate.tram.events.subscriber.DomainEventDispatcher;
import io.eventuate.tram.events.subscriber.DomainEventDispatcherFactory;
import io.eventuate.tram.spring.events.subscriber.TramEventSubscriberConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(TramEventSubscriberConfiguration.class)
public class TripServiceMessageHandlersConfiguration {

  @Bean
  public TripServiceEventConsumer tripServiceEventConsumer() {
    return new TripServiceEventConsumer();
  }

  @Bean
  public DomainEventDispatcher domainEventDispatcher(
      TripServiceEventConsumer tripServiceEventConsumer,
      DomainEventDispatcherFactory domainEventDispatcherFactory) {
    return domainEventDispatcherFactory
        .make("tripServiceEvents", tripServiceEventConsumer.domainEventHandlers());
  }
}

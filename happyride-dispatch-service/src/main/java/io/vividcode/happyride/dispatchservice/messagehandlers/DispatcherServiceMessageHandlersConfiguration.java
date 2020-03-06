package io.vividcode.happyride.dispatchservice.messagehandlers;

import io.eventuate.tram.events.subscriber.DomainEventDispatcher;
import io.eventuate.tram.events.subscriber.DomainEventDispatcherFactory;
import io.eventuate.tram.events.subscriber.TramEventSubscriberConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(TramEventSubscriberConfiguration.class)
public class DispatcherServiceMessageHandlersConfiguration {

  @Bean
  public DispatchServiceEventConsumer dispatcherServiceEventConsumer() {
    return new DispatchServiceEventConsumer();
  }

  @Bean
  public DomainEventDispatcher domainEventDispatcher(
      DispatchServiceEventConsumer dispatchServiceEventConsumer,
      DomainEventDispatcherFactory domainEventDispatcherFactory) {
    return domainEventDispatcherFactory
        .make("dispatcherServiceEvents", dispatchServiceEventConsumer.domainEventHandlers());
  }
}

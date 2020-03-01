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
  public DispatcherServiceEventConsumer dispatcherServiceEventConsumer() {
    return new DispatcherServiceEventConsumer();
  }

  @Bean
  public DomainEventDispatcher domainEventDispatcher(DispatcherServiceEventConsumer dispatcherServiceEventConsumer, DomainEventDispatcherFactory domainEventDispatcherFactory) {
    return domainEventDispatcherFactory.make("dispatcherServiceEvents", dispatcherServiceEventConsumer.domainEventHandlers());
  }
}

package io.vividcode.happyride.dispatchservice.messagehandlers;

import io.eventuate.tram.events.subscriber.DomainEventDispatcher;
import io.eventuate.tram.events.subscriber.DomainEventDispatcherFactory;
import io.eventuate.tram.spring.events.subscriber.TramEventSubscriberConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(TramEventSubscriberConfiguration.class)
public class DispatchServiceMessageHandlersConfiguration {

  @Bean
  public DispatchServiceEventConsumer dispatchServiceEventConsumer() {
    return new DispatchServiceEventConsumer();
  }

  @Bean
  public DomainEventDispatcher domainEventDispatcher(
      DispatchServiceEventConsumer dispatchServiceEventConsumer,
      DomainEventDispatcherFactory domainEventDispatcherFactory) {
    return domainEventDispatcherFactory
        .make("dispatchServiceEvents",
            dispatchServiceEventConsumer.domainEventHandlers());
  }
}

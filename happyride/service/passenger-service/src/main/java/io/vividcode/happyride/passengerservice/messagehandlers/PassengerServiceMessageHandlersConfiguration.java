package io.vividcode.happyride.passengerservice.messagehandlers;

import io.eventuate.tram.consumer.common.DuplicateMessageDetector;
import io.eventuate.tram.consumer.common.NoopDuplicateMessageDetector;
import io.eventuate.tram.events.subscriber.DomainEventDispatcher;
import io.eventuate.tram.events.subscriber.DomainEventDispatcherFactory;
import io.eventuate.tram.spring.events.publisher.TramEventsPublisherConfiguration;
import io.eventuate.tram.spring.events.subscriber.TramEventSubscriberConfiguration;
import io.eventuate.tram.spring.jdbckafka.TramJdbcKafkaConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({TramJdbcKafkaConfiguration.class,
    TramEventsPublisherConfiguration.class,
    TramEventSubscriberConfiguration.class})
public class PassengerServiceMessageHandlersConfiguration {

  @Bean
  public PassengerServiceEventConsumer passengerServiceEventConsumer() {
    return new PassengerServiceEventConsumer();
  }

  @Bean
  public DomainEventDispatcher passengerServiceEventDispatcher(
      final PassengerServiceEventConsumer passengerServiceEventConsumer,
      final DomainEventDispatcherFactory domainEventDispatcherFactory) {
    return domainEventDispatcherFactory
        .make("passengerServiceEvents",
            passengerServiceEventConsumer.domainEventHandlers());
  }

  @Bean
  public DuplicateMessageDetector duplicateMessageDetector() {
    return new NoopDuplicateMessageDetector();
  }
}

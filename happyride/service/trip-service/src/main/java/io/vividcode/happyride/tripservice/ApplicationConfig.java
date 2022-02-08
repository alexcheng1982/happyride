package io.vividcode.happyride.tripservice;

import io.eventuate.tram.events.publisher.DomainEventPublisher;
import io.eventuate.tram.sagas.spring.orchestration.SagaOrchestratorConfiguration;
import io.eventuate.tram.spring.events.publisher.TramEventsPublisherConfiguration;
import io.eventuate.tram.spring.jdbckafka.TramJdbcKafkaConfiguration;
import io.vividcode.happyride.tripservice.commandhandlers.TripCommandHandlersConfiguration;
import io.vividcode.happyride.tripservice.domain.TripDomainEventPublisher;
import io.vividcode.happyride.tripservice.messagehandlers.TripServiceMessageHandlersConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableJpaRepositories
@EnableTransactionManagement
@Import({TramEventsPublisherConfiguration.class,
    TramJdbcKafkaConfiguration.class,
    SagaOrchestratorConfiguration.class,
    TripServiceMessageHandlersConfiguration.class,
    TripCommandHandlersConfiguration.class})
public class ApplicationConfig {

  @Bean
  public TripDomainEventPublisher tripAggregateEventPublisher(
      DomainEventPublisher eventPublisher) {
    return new TripDomainEventPublisher(eventPublisher);
  }
}

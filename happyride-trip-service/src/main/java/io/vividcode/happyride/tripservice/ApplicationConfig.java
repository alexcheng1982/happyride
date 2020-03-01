package io.vividcode.happyride.tripservice;

import io.eventuate.tram.events.publisher.DomainEventPublisher;
import io.eventuate.tram.events.publisher.TramEventsPublisherConfiguration;
import io.eventuate.tram.jdbckafka.TramJdbcKafkaConfiguration;
import io.eventuate.tram.sagas.orchestration.SagaManager;
import io.eventuate.tram.sagas.orchestration.SagaManagerImpl;
import io.eventuate.tram.sagas.orchestration.SagaOrchestratorConfiguration;
import io.vividcode.happyride.tripservice.commandhandlers.TripCommandHandlersConfiguration;
import io.vividcode.happyride.tripservice.domain.TripDomainEventPublisher;
import io.vividcode.happyride.tripservice.messagehandlers.TripServiceMessageHandlersConfiguration;
import io.vividcode.happyride.tripservice.sagas.createtrip.CreateTripSaga;
import io.vividcode.happyride.tripservice.sagas.createtrip.CreateTripSagaState;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableJpaRepositories
@EnableTransactionManagement
@Import({TramEventsPublisherConfiguration.class, TramJdbcKafkaConfiguration.class,
    SagaOrchestratorConfiguration.class,
    TripServiceMessageHandlersConfiguration.class, TripCommandHandlersConfiguration.class})
public class ApplicationConfig {

  @Bean
  public TripDomainEventPublisher tripAggregateEventPublisher(DomainEventPublisher eventPublisher) {
    return new TripDomainEventPublisher(eventPublisher);
  }

  @Bean
  public SagaManager<CreateTripSagaState> createOrderSagaManager(CreateTripSaga saga) {
    return new SagaManagerImpl<>(saga);
  }
}

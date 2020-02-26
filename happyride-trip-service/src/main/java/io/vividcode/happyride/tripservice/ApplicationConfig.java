package io.vividcode.happyride.tripservice;

import io.eventuate.tram.jdbckafka.TramJdbcKafkaConfiguration;
import io.eventuate.tram.events.publisher.DomainEventPublisher;
import io.eventuate.tram.events.publisher.TramEventsPublisherConfiguration;
import io.vividcode.happyride.tripservice.domain.TripDomainEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableJpaRepositories
@EnableTransactionManagement
@Import({TramEventsPublisherConfiguration.class, TramJdbcKafkaConfiguration.class})
public class ApplicationConfig {

  @Bean
  public TripDomainEventPublisher tripAggregateEventPublisher(DomainEventPublisher eventPublisher) {
    return new TripDomainEventPublisher(eventPublisher);
  }
}

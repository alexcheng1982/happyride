package io.vividcode.happyride.dispatcherservice;

import io.eventuate.tram.events.publisher.DomainEventPublisher;
import io.eventuate.tram.events.publisher.TramEventsPublisherConfiguration;
import io.eventuate.tram.jdbckafka.TramJdbcKafkaConfiguration;
import io.vividcode.happyride.dispatcherservice.domain.DispatchDomainEventPublisher;
import io.vividcode.happyride.dispatcherservice.messagehandlers.DispatcherServiceMessageHandlersConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableScheduling
@EnableJpaRepositories
@EnableTransactionManagement
@Import({DispatcherServiceMessageHandlersConfiguration.class,
    TramEventsPublisherConfiguration.class,
    TramJdbcKafkaConfiguration.class})
public class ApplicationConfig {
  @Bean
  public DispatchDomainEventPublisher dispatchAggregateEventPublisher(DomainEventPublisher eventPublisher) {
    return new DispatchDomainEventPublisher(eventPublisher);
  }
}

package io.vividcode.happyride.dispatchservice;

import io.eventuate.tram.events.publisher.DomainEventPublisher;
import io.eventuate.tram.events.publisher.TramEventsPublisherConfiguration;
import io.eventuate.tram.jdbckafka.TramJdbcKafkaConfiguration;
import io.vividcode.happyride.dispatchservice.commandhandlers.DispatchCommandHandlersConfiguration;
import io.vividcode.happyride.dispatchservice.domain.DispatchDomainEventPublisher;
import io.vividcode.happyride.dispatchservice.messagehandlers.DispatcherServiceMessageHandlersConfiguration;
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
    DispatchCommandHandlersConfiguration.class,
    TramEventsPublisherConfiguration.class,
    TramJdbcKafkaConfiguration.class})
public class ApplicationConfig {
  @Bean
  public DispatchDomainEventPublisher dispatchAggregateEventPublisher(DomainEventPublisher eventPublisher) {
    return new DispatchDomainEventPublisher(eventPublisher);
  }
}

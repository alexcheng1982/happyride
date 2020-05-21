package io.vividcode.happyride.tripvalidationservice.domain;

import io.eventuate.tram.commands.consumer.CommandDispatcher;
import io.eventuate.tram.sagas.participant.SagaCommandDispatcherFactory;
import io.eventuate.tram.sagas.spring.participant.SagaParticipantConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@EnableAutoConfiguration
@Import({SagaParticipantConfiguration.class})
@ComponentScan
public class TripValidationServiceConfiguration {

  @Bean
  public CommandDispatcher commandDispatcher(
      final TripValidationServiceCommandHandlers commandHandlers,
      final SagaCommandDispatcherFactory sagaCommandDispatcherFactory) {
    return sagaCommandDispatcherFactory
        .make("tripValidationServiceDispatcher",
            commandHandlers.commandHandlers());
  }
}
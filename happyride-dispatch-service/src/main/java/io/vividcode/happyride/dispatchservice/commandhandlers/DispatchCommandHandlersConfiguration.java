package io.vividcode.happyride.dispatchservice.commandhandlers;

import io.eventuate.tram.sagas.participant.SagaCommandDispatcher;
import io.eventuate.tram.sagas.participant.SagaCommandDispatcherFactory;
import io.eventuate.tram.sagas.participant.SagaParticipantConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({SagaParticipantConfiguration.class})
@ComponentScan
public class DispatchCommandHandlersConfiguration {

  @Bean
  public SagaCommandDispatcher dispatchCommandHandlersDispatcher(
      DispatchCommandHandlers dispatchCommandHandlers,
      SagaCommandDispatcherFactory sagaCommandDispatcherFactory) {
    return sagaCommandDispatcherFactory
        .make("dispatchService", dispatchCommandHandlers.commandHandlers());
  }
}

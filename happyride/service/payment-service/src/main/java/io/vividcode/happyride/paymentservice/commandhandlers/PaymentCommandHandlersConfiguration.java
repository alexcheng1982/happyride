package io.vividcode.happyride.paymentservice.commandhandlers;

import io.eventuate.tram.sagas.participant.SagaCommandDispatcher;
import io.eventuate.tram.sagas.participant.SagaCommandDispatcherFactory;
import io.eventuate.tram.sagas.spring.participant.SagaParticipantConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({SagaParticipantConfiguration.class})
@ComponentScan
public class PaymentCommandHandlersConfiguration {

  @Bean
  public SagaCommandDispatcher paymentCommandHandlersDispatcher(
      final PaymentCommandHandlers paymentCommandHandlers,
      final SagaCommandDispatcherFactory sagaCommandDispatcherFactory) {
    return sagaCommandDispatcherFactory
        .make("paymentService", paymentCommandHandlers.commandHandlers());
  }
}

package io.vividcode.happyride.tripservice.commandsample;

import io.eventuate.tram.commands.consumer.CommandDispatcher;
import io.eventuate.tram.commands.consumer.CommandDispatcherFactory;
import io.eventuate.tram.spring.commands.consumer.TramCommandConsumerConfiguration;
import io.eventuate.tram.spring.commands.producer.TramCommandProducerConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({TramCommandProducerConfiguration.class,
    TramCommandConsumerConfiguration.class})
public class EchoConfiguration {

  @Bean
  public CommandDispatcher echoCommandDispatcher(
      CommandDispatcherFactory commandDispatcherFactory,
      EchoCommandHandlers echoCommandHandlers) {
    return commandDispatcherFactory
        .make("echoCommandDispatcher",
            echoCommandHandlers.commandHandlers());
  }
}

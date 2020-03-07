package io.vividcode.happyride.tripvalidationservice.domain;

import static io.eventuate.tram.commands.consumer.CommandHandlerReplyBuilder.withFailure;
import static io.eventuate.tram.commands.consumer.CommandHandlerReplyBuilder.withSuccess;

import io.eventuate.tram.commands.consumer.CommandHandlers;
import io.eventuate.tram.commands.consumer.CommandMessage;
import io.eventuate.tram.messaging.common.Message;
import io.eventuate.tram.sagas.participant.SagaCommandHandlersBuilder;
import io.vividcode.happyride.tripvalidationservice.api.TripValidationServiceChannels;
import io.vividcode.happyride.tripvalidationservice.api.ValidateTripCommand;
import io.vividcode.happyride.tripvalidationservice.service.TripValidationException;
import io.vividcode.happyride.tripvalidationservice.service.TripValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TripValidationServiceCommandHandlers {

  @Autowired
  TripValidationService tripValidationService;

  public CommandHandlers commandHandlers() {
    return SagaCommandHandlersBuilder
        .fromChannel(TripValidationServiceChannels.tripValidationServiceChannel)
        .onMessage(ValidateTripCommand.class, this::validateOrderForConsumer)
        .build();
  }

  private Message validateOrderForConsumer(CommandMessage<ValidateTripCommand> cm) {
    try {
      tripValidationService.validateTrip(cm.getCommand().getTripDetails());
      return withSuccess();
    } catch (TripValidationException e) {
      return withFailure(e);
    }
  }
}

package io.vividcode.happyride.tripvalidationservice.domain;

import static io.eventuate.tram.commands.consumer.CommandHandlerReplyBuilder.withFailure;
import static io.eventuate.tram.commands.consumer.CommandHandlerReplyBuilder.withSuccess;

import io.eventuate.tram.commands.consumer.CommandHandlers;
import io.eventuate.tram.commands.consumer.CommandMessage;
import io.eventuate.tram.messaging.common.Message;
import io.eventuate.tram.sagas.participant.SagaCommandHandlersBuilder;
import io.vividcode.happyride.tripvalidationservice.api.InvalidTripReply;
import io.vividcode.happyride.tripvalidationservice.api.TripValidationServiceChannels;
import io.vividcode.happyride.tripvalidationservice.api.ValidateTripCommand;
import io.vividcode.happyride.tripvalidationservice.service.TripValidationException;
import io.vividcode.happyride.tripvalidationservice.service.TripValidationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class TripValidationServiceCommandHandlers {

  @Autowired
  TripValidationService tripValidationService;

  public CommandHandlers commandHandlers() {
    return SagaCommandHandlersBuilder
        .fromChannel(TripValidationServiceChannels.tripValidation)
        .onMessage(ValidateTripCommand.class, this::validateTrip)
        .build();
  }

  private Message validateTrip(final CommandMessage<ValidateTripCommand> cm) {
    try {
      this.tripValidationService.validateTrip(cm.getCommand().getTripDetails());
      return withSuccess();
    } catch (final TripValidationException e) {
      log.warn("Trip is not valid", e);
      return withFailure(new InvalidTripReply());
    }
  }
}

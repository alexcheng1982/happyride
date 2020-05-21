package io.vividcode.happyride.tripservice.sagaparticipants;

import io.eventuate.tram.commands.common.Success;
import io.eventuate.tram.sagas.simpledsl.CommandEndpoint;
import io.eventuate.tram.sagas.simpledsl.CommandEndpointBuilder;
import io.vividcode.happyride.tripvalidationservice.api.InvalidTripReply;
import io.vividcode.happyride.tripvalidationservice.api.TripValidationServiceChannels;
import io.vividcode.happyride.tripvalidationservice.api.ValidateTripCommand;
import org.springframework.stereotype.Component;

@Component
public class TripValidationServiceProxy {

  public final CommandEndpoint<ValidateTripCommand> validateTrip = CommandEndpointBuilder
      .forCommand(ValidateTripCommand.class)
      .withChannel(TripValidationServiceChannels.tripValidation)
      .withReply(Success.class)
      .withReply(InvalidTripReply.class)
      .build();
}

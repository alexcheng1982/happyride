package io.vividcode.happyride.tripservice.sagaparticipants;

import io.eventuate.tram.commands.common.Success;
import io.eventuate.tram.sagas.simpledsl.CommandEndpoint;
import io.eventuate.tram.sagas.simpledsl.CommandEndpointBuilder;
import io.vividcode.happyride.tripservice.api.TripServiceChannels;
import org.springframework.stereotype.Component;

@Component
public class TripServiceProxy {

  public final CommandEndpoint<RejectTripCommand> reject = CommandEndpointBuilder
      .forCommand(RejectTripCommand.class)
      .withChannel(TripServiceChannels.tripServiceChannel)
      .withReply(Success.class)
      .build();

  public final CommandEndpoint<ConfirmTripCommand> confirm = CommandEndpointBuilder
      .forCommand(ConfirmTripCommand.class)
      .withChannel(TripServiceChannels.tripServiceChannel)
      .withReply(Success.class)
      .build();
}

package io.vividcode.happyride.tripservice.sagaparticipants;

import io.eventuate.tram.commands.common.Success;
import io.eventuate.tram.sagas.simpledsl.CommandEndpoint;
import io.eventuate.tram.sagas.simpledsl.CommandEndpointBuilder;
import io.vividcode.happyride.dispatchservice.api.DispatchServiceChannels;
import io.vividcode.happyride.dispatchservice.api.events.VerifyDispatchCommand;
import org.springframework.stereotype.Component;

@Component
public class DispatchServiceProxy {
  public final CommandEndpoint<VerifyDispatchCommand> verifyDispatch = CommandEndpointBuilder
      .forCommand(VerifyDispatchCommand.class)
      .withChannel(DispatchServiceChannels.dispatchServiceChannel)
      .withReply(Success.class)
      .build();
}

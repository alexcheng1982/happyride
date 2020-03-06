package io.vividcode.happyride.dispatchservice.commandhandlers;


import static io.eventuate.tram.commands.consumer.CommandHandlerReplyBuilder.withSuccess;

import io.eventuate.tram.commands.consumer.CommandHandlers;
import io.eventuate.tram.commands.consumer.CommandMessage;
import io.eventuate.tram.messaging.common.Message;
import io.eventuate.tram.sagas.participant.SagaCommandHandlersBuilder;
import io.vividcode.happyride.dispatchservice.api.DispatchServiceChannels;
import io.vividcode.happyride.dispatchservice.api.events.VerifyDispatchCommand;
import io.vividcode.happyride.dispatchservice.DispatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DispatchCommandHandlers {

  @Autowired
  DispatchService dispatchService;

  public CommandHandlers commandHandlers() {
    return SagaCommandHandlersBuilder
        .fromChannel(DispatchServiceChannels.dispatchServiceChannel)
        .onMessage(VerifyDispatchCommand.class, this::verifyDispatch)
        .build();
  }

  private Message verifyDispatch(CommandMessage<VerifyDispatchCommand> cm) {
    dispatchService.verifyDispatch(cm.getCommand().getTripDetails());
    return withSuccess();
  }
}

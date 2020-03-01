package io.vividcode.happyride.dispatcherservice.commandhandlers;


import static io.eventuate.tram.commands.consumer.CommandHandlerReplyBuilder.withSuccess;

import io.eventuate.tram.commands.consumer.CommandHandlers;
import io.eventuate.tram.commands.consumer.CommandMessage;
import io.eventuate.tram.messaging.common.Message;
import io.eventuate.tram.sagas.participant.SagaCommandHandlersBuilder;
import io.vividcode.happyride.dispatcherservice.api.DispatchServiceChannels;
import io.vividcode.happyride.dispatcherservice.api.events.VerifyDispatchCommand;
import io.vividcode.happyride.dispatcherservice.service.DispatcherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DispatchCommandHandlers {

  @Autowired
  DispatcherService dispatcherService;

  public CommandHandlers commandHandlers() {
    return SagaCommandHandlersBuilder
        .fromChannel(DispatchServiceChannels.dispatchServiceChannel)
        .onMessage(VerifyDispatchCommand.class, this::verifyDispatch)
        .build();
  }

  private Message verifyDispatch(CommandMessage<VerifyDispatchCommand> cm) {
    dispatcherService.verifyDispatch(cm.getCommand().getTripDetails());
    return withSuccess();
  }
}
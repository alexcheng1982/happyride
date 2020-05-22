package io.vividcode.happyride.dispatchservice.commandhandlers;


import static io.eventuate.tram.commands.consumer.CommandHandlerReplyBuilder.withFailure;
import static io.eventuate.tram.commands.consumer.CommandHandlerReplyBuilder.withSuccess;

import io.eventuate.tram.commands.consumer.CommandHandlers;
import io.eventuate.tram.commands.consumer.CommandMessage;
import io.eventuate.tram.messaging.common.Message;
import io.eventuate.tram.sagas.participant.SagaCommandHandlersBuilder;
import io.vividcode.happyride.dispatchservice.DispatchService;
import io.vividcode.happyride.dispatchservice.DispatchVerificationException;
import io.vividcode.happyride.dispatchservice.api.DispatchServiceChannels;
import io.vividcode.happyride.dispatchservice.api.events.InvalidDispatchRequestReply;
import io.vividcode.happyride.dispatchservice.api.events.VerifyDispatchCommand;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class DispatchCommandHandlers {

  @Autowired
  DispatchService dispatchService;

  public CommandHandlers commandHandlers() {
    return SagaCommandHandlersBuilder
        .fromChannel(DispatchServiceChannels.dispatchServiceChannel)
        .onMessage(VerifyDispatchCommand.class, this::verifyDispatch)
        .build();
  }

  private Message verifyDispatch(final CommandMessage<VerifyDispatchCommand> cm) {
    try {
      this.dispatchService.verifyDispatch(cm.getCommand().getTripDetails());
      return withSuccess();
    } catch (final DispatchVerificationException e) {
      log.warn("Dispatch is not valid", e);
      return withFailure(new InvalidDispatchRequestReply());
    }
  }
}

package io.vividcode.happyride.tripservice.commandsample;

import static io.eventuate.tram.commands.consumer.CommandHandlerReplyBuilder.withSuccess;

import io.eventuate.tram.commands.consumer.CommandHandlers;
import io.eventuate.tram.commands.consumer.CommandHandlersBuilder;
import io.eventuate.tram.commands.consumer.CommandMessage;
import io.eventuate.tram.commands.consumer.PathVariables;
import io.eventuate.tram.messaging.common.Message;
import org.springframework.stereotype.Component;

@Component
public class EchoCommandHandlers {

  public CommandHandlers commandHandlers() {
    return CommandHandlersBuilder.fromChannel("echo")
        .resource("/user/{username}")
        .onMessage(EchoCommand.class, this::echo)
        .build();
  }

  private Message echo(CommandMessage<EchoCommand> cm,
      PathVariables pathVariables) {
    return withSuccess("echo -> " + pathVariables.getString("username"));
  }
}

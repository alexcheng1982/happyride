package io.vividcode.happyride.tripservice.commandsample;

import static io.eventuate.tram.commands.consumer.CommandHandlerReplyBuilder.withSuccess;

import io.eventuate.tram.commands.consumer.CommandHandlers;
import io.eventuate.tram.commands.consumer.CommandHandlersBuilder;
import io.eventuate.tram.commands.consumer.CommandMessage;
import io.eventuate.tram.commands.consumer.PathVariables;
import io.eventuate.tram.messaging.common.Message;
import org.springframework.stereotype.Component;

@Component
public class WeatherCommandHandlers {

  public CommandHandlers commandHandlers() {
    return CommandHandlersBuilder.fromChannel("weather")
        .onMessage(QueryWeatherCommand.class, this::queryWeather)
        .build();
  }

  private Message queryWeather(CommandMessage<QueryWeatherCommand> cm,
      PathVariables pathVariables) {
    return withSuccess(
        new QueryWeatherResult(cm.getCommand().getCity(), "Rain"));
  }
}

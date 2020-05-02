package io.vividcode.happyride.tripservice.commandsample;

import com.google.common.collect.ImmutableMap;
import io.eventuate.common.json.mapper.JSonMapper;
import io.eventuate.tram.commands.producer.CommandProducer;
import io.eventuate.tram.messaging.consumer.MessageConsumer;
import java.util.Collections;
import java.util.HashMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class CommandTests {

  @Autowired
  CommandProducer commandProducer;

  @Autowired
  MessageConsumer messageConsumer;

  @EventListener(ApplicationReadyEvent.class)
  public void testWeatherCommand() {
    messageConsumer
        .subscribe("weather-subscriber", Collections.singleton("weather-reply"),
            message -> {
              QueryWeatherResult result = JSonMapper
                  .fromJson(message.getPayload(), QueryWeatherResult.class);
              System.out.println(result.getResult());
            });

    commandProducer
        .send("weather", new QueryWeatherCommand("Beijing"), "weather-reply",
            new HashMap<>());
  }

  @EventListener(ApplicationReadyEvent.class)
  public void testEchoCommand() {
    messageConsumer
        .subscribe("echo-subscriber", Collections.singleton("echo-reply"),
            message -> {
              System.out.println(message.getPayload());
            });

    commandProducer
        .send("echo", "/user/alex", new EchoCommand(), "echo-reply",
            new HashMap<>());
  }
}

package io.vividcode.happyride.tripservice.commandsample;

import io.eventuate.tram.commands.common.Command;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@NoArgsConstructor
@RequiredArgsConstructor
public class QueryWeatherCommand implements Command {

  @NonNull
  private String city;
}

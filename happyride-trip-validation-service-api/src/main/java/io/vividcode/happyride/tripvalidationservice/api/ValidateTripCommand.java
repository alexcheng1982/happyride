package io.vividcode.happyride.tripvalidationservice.api;

import io.eventuate.tram.commands.common.Command;
import io.vividcode.happyride.tripservice.api.events.TripDetails;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@NoArgsConstructor
public class ValidateTripCommand implements Command {
  @NonNull
  private TripDetails tripDetails;
}

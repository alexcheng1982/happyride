package io.vividcode.happyride.dispatchservice.api.events;

import io.eventuate.tram.commands.common.Command;
import io.vividcode.happyride.tripservice.api.events.TripDetails;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@NoArgsConstructor
@RequiredArgsConstructor
public class VerifyDispatchCommand implements Command {
  @NonNull
  private TripDetails tripDetails;
}

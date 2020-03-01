package io.vividcode.happyride.tripservice.sagaparticipants;

import io.eventuate.tram.commands.common.Command;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@NoArgsConstructor
@RequiredArgsConstructor
@EqualsAndHashCode
@Getter
public abstract class TripCommand implements Command {

  @NonNull
  private String tripId;
}

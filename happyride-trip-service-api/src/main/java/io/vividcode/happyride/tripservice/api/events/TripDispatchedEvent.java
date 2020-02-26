package io.vividcode.happyride.tripservice.api.events;

import java.util.Set;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
public class TripDispatchedEvent implements TripDomainEvent {
  @NonNull
  private TripDetails tripDetails;

  @NonNull
  private Set<String> drivers;
}

package io.vividcode.happyride.dispatchservice.api.events;

import io.vividcode.happyride.tripservice.api.events.TripDetails;
import java.util.Set;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@NoArgsConstructor
@RequiredArgsConstructor
public class TripDispatchedEvent implements DispatchDomainEvent {
  @NonNull
  private String tripId;

  @NonNull
  private TripDetails tripDetails;

  @NonNull
  private Set<String> drivers;
}

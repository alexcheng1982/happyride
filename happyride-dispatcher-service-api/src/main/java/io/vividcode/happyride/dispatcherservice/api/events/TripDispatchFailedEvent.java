package io.vividcode.happyride.dispatcherservice.api.events;

import lombok.Data;
import lombok.NonNull;

@Data
public class TripDispatchFailedEvent implements DispatchDomainEvent {
  @NonNull
  private String tripId;

  @NonNull
  private TripDispatchFailedReason reason;
}

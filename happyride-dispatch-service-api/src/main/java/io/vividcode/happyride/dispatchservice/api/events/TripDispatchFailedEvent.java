package io.vividcode.happyride.dispatchservice.api.events;

import lombok.Data;
import lombok.NonNull;

@Data
public class TripDispatchFailedEvent implements DispatchDomainEvent {

  @NonNull
  private String tripId;

  @NonNull
  private TripDispatchFailedReason reason;
}

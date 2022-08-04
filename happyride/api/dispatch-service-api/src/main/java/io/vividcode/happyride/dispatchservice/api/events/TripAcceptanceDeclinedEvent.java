package io.vividcode.happyride.dispatchservice.api.events;

import lombok.Data;
import lombok.NonNull;

@Data
public class TripAcceptanceDeclinedEvent implements DispatchDomainEvent {
  @NonNull
  private String tripId;

  @NonNull
  private String driverId;

  @NonNull
  private TripAcceptanceDeclinedReason reason;
}

package io.vividcode.happyride.dispatcherservice.api.events;

import lombok.Data;
import lombok.NonNull;

@Data
public class TripAcceptanceDeclinedEvent implements DispatchDomainEvent {
  @NonNull
  private String driverId;

  @NonNull
  private String reason;
}

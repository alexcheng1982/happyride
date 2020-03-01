package io.vividcode.happyride.dispatchservice.api.events;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@NoArgsConstructor
@RequiredArgsConstructor
public class TripAcceptanceAcceptedEvent implements DispatchDomainEvent {
  @NonNull
  private String tripId;

  @NonNull
  private String driverId;
}

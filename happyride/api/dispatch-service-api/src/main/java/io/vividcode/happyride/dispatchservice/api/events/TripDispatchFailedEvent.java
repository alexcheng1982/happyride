package io.vividcode.happyride.dispatchservice.api.events;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@NoArgsConstructor
@RequiredArgsConstructor
public class TripDispatchFailedEvent implements DispatchDomainEvent {

  @NonNull
  private String tripId;

  @NonNull
  private TripDispatchFailedReason reason;
}

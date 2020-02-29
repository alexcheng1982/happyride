package io.vividcode.happyride.tripservice.api.events;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@NoArgsConstructor
public class TripFinishedEvent implements TripDomainEvent {
  @NonNull
  private String tripId;
}

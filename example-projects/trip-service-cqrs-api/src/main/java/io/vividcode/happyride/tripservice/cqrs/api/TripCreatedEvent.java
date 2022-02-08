package io.vividcode.happyride.tripservice.cqrs.api;

import io.vividcode.happyride.tripservice.api.events.TripDetails;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@NoArgsConstructor
@RequiredArgsConstructor
public class TripCreatedEvent {
  @NonNull
  private String tripId;

  @NonNull
  private TripDetails tripDetails;
}

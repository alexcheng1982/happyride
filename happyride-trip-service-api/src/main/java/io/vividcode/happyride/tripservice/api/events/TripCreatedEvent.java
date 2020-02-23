package io.vividcode.happyride.tripservice.api.events;

import lombok.Data;
import lombok.NonNull;

@Data
public class TripCreatedEvent implements TripDomainEvent {
  @NonNull
  private TripDetails tripDetails;
}

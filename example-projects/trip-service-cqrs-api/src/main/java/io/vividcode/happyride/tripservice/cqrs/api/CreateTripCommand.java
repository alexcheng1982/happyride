package io.vividcode.happyride.tripservice.cqrs.api;

import io.vividcode.happyride.tripservice.api.events.TripDetails;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

@Data
@NoArgsConstructor
@RequiredArgsConstructor
public class CreateTripCommand {
  @NonNull
  @TargetAggregateIdentifier
  private String tripId;

  @NonNull
  private TripDetails tripDetails;
}

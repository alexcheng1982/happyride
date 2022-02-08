package io.vividcode.happyride.tripservice.cqrs.api;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

@Data
@NoArgsConstructor
@RequiredArgsConstructor
public class CancelTripCommand {
  @NonNull
  @TargetAggregateIdentifier
  private String tripId;
}

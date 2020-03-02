package io.vividcode.happyride.tripservice.cqrs.api;

import io.vividcode.happyride.tripservice.api.TripState;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TripSummary {

  private String id;

  private PositionView startPos;

  private PositionView endPos;

  private TripState state;
}

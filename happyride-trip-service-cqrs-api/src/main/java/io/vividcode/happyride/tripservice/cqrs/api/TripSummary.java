package io.vividcode.happyride.tripservice.cqrs.api;

import io.vividcode.happyride.common.PositionVO;
import io.vividcode.happyride.tripservice.api.TripState;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TripSummary {

  private String id;

  private PositionVO startPos;

  private PositionVO endPos;

  private TripState state;
}

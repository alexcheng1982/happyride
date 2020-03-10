package io.vividcode.happyride.tripservice.api.events;


import io.vividcode.happyride.common.PositionView;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@NoArgsConstructor
@RequiredArgsConstructor
public class TripDetails {

  @NonNull
  private String passengerId;

  private String driverId;

  @NonNull
  private PositionView startPos;

  @NonNull
  private PositionView endPos;
}

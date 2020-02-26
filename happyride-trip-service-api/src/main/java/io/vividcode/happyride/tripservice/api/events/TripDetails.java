package io.vividcode.happyride.tripservice.api.events;


import io.vividcode.happyride.common.Position;
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
  private Position startPos;

  @NonNull
  private Position endPos;
}

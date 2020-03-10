package io.vividcode.happyride.tripservice.api.web;


import io.vividcode.happyride.common.PositionView;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@NoArgsConstructor
@RequiredArgsConstructor
public class CreateTripRequest {

  @NonNull
  private String passengerId;

  @NonNull
  private PositionView startPos;

  @NonNull
  private PositionView endPos;

}

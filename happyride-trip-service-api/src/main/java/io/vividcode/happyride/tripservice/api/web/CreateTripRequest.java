package io.vividcode.happyride.tripservice.api.web;


import io.vividcode.happyride.common.PositionView;
import lombok.Data;

@Data
public class CreateTripRequest {

  private String passengerId;

  private PositionView startPos;

  private PositionView endPos;

}

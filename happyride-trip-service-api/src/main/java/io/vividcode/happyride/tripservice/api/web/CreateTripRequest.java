package io.vividcode.happyride.tripservice.api.web;


import io.vividcode.happyride.common.Position;
import lombok.Data;

@Data
public class CreateTripRequest {
  private String passengerId;

  private Position startPos;

  private Position endPos;

}

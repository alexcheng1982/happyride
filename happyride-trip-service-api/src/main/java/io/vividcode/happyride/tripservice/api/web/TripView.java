package io.vividcode.happyride.tripservice.api.web;

import io.vividcode.happyride.common.PositionView;
import lombok.Data;

@Data
public class TripView {
  private String id;

  private String passengerId;

  private String driverId;

  private PositionView startPos;

  private PositionView endPos;

  private String state;
}

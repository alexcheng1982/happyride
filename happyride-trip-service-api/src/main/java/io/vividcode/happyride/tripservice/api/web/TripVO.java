package io.vividcode.happyride.tripservice.api.web;

import io.vividcode.happyride.common.PositionVO;
import lombok.Data;

@Data
public class TripVO {
  private String id;

  private String passengerId;

  private String driverId;

  private PositionVO startPos;

  private PositionVO endPos;

  private String state;
}

package io.vividcode.happyride.tripservice.api.web;

import lombok.Data;

@Data
public class AcceptTripRequest {
  private String tripId;

  private String driverId;
}

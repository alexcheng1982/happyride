package io.vividcode.happyride.tripservice.api.web;

import java.math.BigDecimal;
import lombok.Data;

@Data
public class AcceptTripRequest {

  private String driverId;

  private BigDecimal posLng;

  private BigDecimal posLat;
}

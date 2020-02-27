package io.vividcode.happyride.tripservice.api.web;

import java.math.BigDecimal;
import lombok.Data;

@Data
public class AcceptTripRequest {
  private String tripId;

  private String driverId;

  private BigDecimal lng;

  private BigDecimal lat;
}

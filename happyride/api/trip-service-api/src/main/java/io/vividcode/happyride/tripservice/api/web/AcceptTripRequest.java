package io.vividcode.happyride.tripservice.api.web;

import java.math.BigDecimal;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@NoArgsConstructor
@RequiredArgsConstructor
public class AcceptTripRequest {

  @NonNull
  private String driverId;

  @NonNull
  private BigDecimal posLng;

  @NonNull
  private BigDecimal posLat;
}

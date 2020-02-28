package io.vividcode.happyride.tripservice.api.events;

import java.math.BigDecimal;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@NoArgsConstructor
public class DriverAcceptTripDetails {

  @NonNull
  private String driverId;

  @NonNull
  private BigDecimal posLng;

  @NonNull
  private BigDecimal posLat;
}

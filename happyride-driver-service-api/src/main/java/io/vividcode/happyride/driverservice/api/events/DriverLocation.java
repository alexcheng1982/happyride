package io.vividcode.happyride.driverservice.api.events;

import java.math.BigDecimal;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class DriverLocation {
  @NonNull
  private String driverId;

  @NonNull
  private BigDecimal lat;

  @NonNull
  private BigDecimal lng;

  public DriverLocation moveTo(BigDecimal latDelta, BigDecimal lngDelta) {
    return new DriverLocation(driverId, lat.add(latDelta), lng.add(lngDelta));
  }
}

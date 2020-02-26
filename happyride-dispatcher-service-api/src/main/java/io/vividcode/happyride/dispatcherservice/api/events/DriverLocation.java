package io.vividcode.happyride.dispatcherservice.api.events;

import java.math.BigDecimal;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@NoArgsConstructor
public class DriverLocation {
  @NonNull
  private String driverId;

  @NonNull
  private String vehicleId;

  @NonNull
  private BigDecimal lng;

  @NonNull
  private BigDecimal lat;

  public DriverLocation moveTo(BigDecimal lngDelta, BigDecimal latDelta) {
     return new DriverLocation(driverId, vehicleId, lng.add(lngDelta), lat.add(latDelta));
  }
}

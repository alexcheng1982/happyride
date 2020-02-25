package io.vividcode.happyride.dispatcherservice.api.events;

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
  private double lng;

  @NonNull
  private double lat;

  public DriverLocation moveTo(double lngDelta, double latDelta) {
     return new DriverLocation(driverId, vehicleId, lng + lngDelta, lat + latDelta);
  }
}

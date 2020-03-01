package io.vividcode.happyride.driversimulator;

import io.vividcode.happyride.common.DriverState;
import java.math.BigDecimal;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DriverSimulatorSnapshot {

  private String id;
  private String driverId;
  private String vehicleId;
  private DriverState state;
  private BigDecimal posLng;
  private BigDecimal posLat;
}

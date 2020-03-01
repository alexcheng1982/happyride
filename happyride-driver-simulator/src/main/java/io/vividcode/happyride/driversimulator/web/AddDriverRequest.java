package io.vividcode.happyride.driversimulator.web;

import java.math.BigDecimal;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@NoArgsConstructor
@RequiredArgsConstructor
public class AddDriverRequest {
  @NonNull
  private String driverId;

  @NonNull
  private BigDecimal posLng = BigDecimal.ZERO;

  @NonNull
  private BigDecimal posLat = BigDecimal.ZERO;

  private boolean startSimulation = true;
}

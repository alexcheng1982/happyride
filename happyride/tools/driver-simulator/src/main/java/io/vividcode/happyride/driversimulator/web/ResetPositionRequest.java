package io.vividcode.happyride.driversimulator.web;

import java.math.BigDecimal;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
public class ResetPositionRequest {

  @NonNull
  private BigDecimal posLng = BigDecimal.ZERO;

  @NonNull
  private BigDecimal posLat = BigDecimal.ZERO;
}

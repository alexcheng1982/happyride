package io.vividcode.happyride.dispatchservice;

import java.math.BigDecimal;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class AvailableDriver {

  @NonNull
  private String driverId;

  @NonNull
  private BigDecimal posLng;

  @NonNull
  private BigDecimal posLat;
}

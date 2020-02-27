package io.vividcode.happyride.dispatcherservice.service;

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
  private BigDecimal lng;

  @NonNull
  private BigDecimal lat;
}

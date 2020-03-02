package io.vividcode.happyride.tripservice.cqrs.api;

import java.math.BigDecimal;
import lombok.Data;
import lombok.NonNull;

@Data
public class PositionView {
  @NonNull
  private BigDecimal lng;

  @NonNull
  private BigDecimal lat;
}

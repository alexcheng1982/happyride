package io.vividcode.happyride.common;

import java.math.BigDecimal;
import lombok.Data;

@Data
public class PositionView {
  private BigDecimal lng;

  private BigDecimal lat;

  private String addressId;
}

package io.vividcode.happyride.common;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
@NoArgsConstructor
public class PositionView {
  @NonNull
  private BigDecimal lng;

  @NonNull
  private BigDecimal lat;

  private String addressId;

  public Position deserialize() {
    return new Position(lng, lat, addressId);
  }
}

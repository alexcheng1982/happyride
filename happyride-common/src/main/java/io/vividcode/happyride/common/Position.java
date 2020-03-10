package io.vividcode.happyride.common;

import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Embeddable
@Data
@NoArgsConstructor
@RequiredArgsConstructor
@AllArgsConstructor
public class Position {

  @Column(name = "lng")
  @NonNull
  private BigDecimal lng;

  @Column(name = "lat")
  @NonNull
  private BigDecimal lat;

  @Column(name = "address_id", length = 36)
  private String addressId;

  public PositionView serialize() {
    return new PositionView(lng, lat, addressId);
  }
}

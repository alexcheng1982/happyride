package io.vividcode.happyride.common;

import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Embeddable
@Data
@NoArgsConstructor
public class Position {

  @Column(name = "lat")
  @NonNull
  private BigDecimal lat;

  @Column(name = "lng")
  @NonNull
  private BigDecimal lng;

  @Column(name = "address_id", length = 36)
  private String addressId;
}

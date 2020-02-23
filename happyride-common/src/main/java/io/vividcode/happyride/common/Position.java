package io.vividcode.happyride.common;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@NoArgsConstructor
public class Position {

  @Column(name = "lat")
  private Double lat;

  @Column(name = "lng")
  private Double lng;

  @Column(name = "address_id")
  private String addressId;
}

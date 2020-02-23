package io.vividcode.happyride.address.domain;

import io.vividcode.happyride.common.BaseEntityWithGeneratedId;
import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "address")
@Getter
@Setter
@NoArgsConstructor
@ToString
public class Address extends BaseEntityWithGeneratedId {

  @ManyToOne
  @JoinColumn(name = "area_id")
  private Area area;

  @Column(name = "address_line")
  private String addressLine;

  @Column(name = "lat", precision = 10, scale = 6)
  private BigDecimal lat;

  @Column(name = "lng", precision = 10, scale = 6)
  private BigDecimal lng;
}

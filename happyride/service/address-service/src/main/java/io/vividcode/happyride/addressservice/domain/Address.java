package io.vividcode.happyride.addressservice.domain;

import io.vividcode.happyride.common.BaseEntityWithGeneratedId;
import java.math.BigDecimal;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "addresses")
@Getter
@Setter
@NoArgsConstructor
@ToString
public class Address extends BaseEntityWithGeneratedId {

  @ManyToOne
  @JoinColumn(name = "area_id")
  private Area area;

  @Column(name = "address_line")
  @Size(max = 255)
  private String addressLine;

  @Column(name = "lng")
  private BigDecimal lng;

  @Column(name = "lat")
  private BigDecimal lat;
}

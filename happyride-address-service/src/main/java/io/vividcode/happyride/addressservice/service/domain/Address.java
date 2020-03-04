package io.vividcode.happyride.addressservice.service.domain;

import io.vividcode.happyride.common.BaseEntityWithGeneratedId;
import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Size;
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

  @Column(name = "lat")
  private BigDecimal lat;

  @Column(name = "lng")
  private BigDecimal lng;
}

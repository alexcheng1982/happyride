package io.vividcode.happyride.addressservice.model;

import com.google.common.collect.ImmutableMap;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import java.math.BigDecimal;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Size;

@Entity
@Table(name = "addresses")
public class Address extends PanacheEntityBase {

  @Id
  public String id;

  @ManyToOne
  @JoinColumn(name = "area_id")
  public Area area;

  @Column(name = "address_line")
  @Size(max = 255)
  public String addressLine;

  @Column(name = "lng")
  public BigDecimal lng;

  @Column(name = "lat")
  public BigDecimal lat;

  public static List<Address> findByAreaCodeAndAddressLine(Long areaCode,
      String query) {
    return list("area.areaCode = :areaCode and addressLine LIKE :query",
        ImmutableMap.of(
            "areaCode", areaCode,
            "query", "%" + query + "%"
        ));
  }
}

package io.vividcode.happyride.addressservice.model;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import java.math.BigDecimal;
import java.util.Optional;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Size;

@Entity
@Table(name = "areas")
public class Area extends PanacheEntityBase {

  @Id
  @GeneratedValue
  public Integer id;

  @Column(name = "level")
  public Integer level;

  @Column(name = "parent_code")
  public Long parentCode;

  @Column(name = "area_code")
  public Long areaCode;

  @Column(name = "zip_code")
  @Size(max = 6)
  public String zipCode;

  @Column(name = "city_code")
  @Size(max = 6)
  public String cityCode;

  @Column(name = "name")
  @Size(max = 50)
  public String name;

  @Column(name = "short_name")
  @Size(max = 50)
  public String shortName;

  @Column(name = "merger_name")
  @Size(max = 50)
  public String mergerName;

  @Column(name = "pinyin")
  @Size(max = 30)
  public String pinyin;

  @Column(name = "lat")
  public BigDecimal lat;

  @Column(name = "lng")
  public BigDecimal lng;

  public static Optional<Area> findByAreaCode(Long areaCode) {
    return find("areaCode", areaCode).firstResultOptional();
  }
}

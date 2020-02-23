package io.vividcode.happyride.address.domain;

import io.vividcode.happyride.common.AbstractEntity;
import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "area")
@Getter
@Setter
@NoArgsConstructor
@ToString
public class Area extends AbstractEntity<Integer> {

  @Id
  @Column(name = "id")
  @GeneratedValue
  private Integer id;

  @Column(name = "level")
  private int level;

  @Column(name = "parent_code")
  private Long parentCode;

  @Column(name = "area_code")
  private Long areaCode;

  @Column(name = "zip_code", length = 6)
  private String zipCode;

  @Column(name = "city_code", length = 6)
  private String cityCode;

  @Column(name = "name", length = 50)
  private String name;

  @Column(name = "short_name", length = 50)
  private String shortName;

  @Column(name = "merger_name", length = 50)
  private String mergerName;

  @Column(name = "pinyin", length = 30)
  private String pinyin;

  @Column(name = "lat")
  private BigDecimal lat;

  @Column(name = "lng")
  private BigDecimal lng;
}

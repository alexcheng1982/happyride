package io.vividcode.happyride.addressservice.domain;

import io.vividcode.happyride.common.AbstractEntity;
import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "areas")
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
  private Integer level;

  @Column(name = "parent_code")
  private Long parentCode;

  @Column(name = "area_code")
  private Long areaCode;

  @Column(name = "zip_code")
  @Size(max = 6)
  private String zipCode;

  @Column(name = "city_code")
  @Size(max = 6)
  private String cityCode;

  @Column(name = "name")
  @Size(max = 50)
  private String name;

  @Column(name = "short_name")
  @Size(max = 50)
  private String shortName;

  @Column(name = "merger_name")
  @Size(max = 50)
  private String mergerName;

  @Column(name = "pinyin")
  @Size(max = 30)
  private String pinyin;

  @Column(name = "lat")
  private BigDecimal lat;

  @Column(name = "lng")
  private BigDecimal lng;
}

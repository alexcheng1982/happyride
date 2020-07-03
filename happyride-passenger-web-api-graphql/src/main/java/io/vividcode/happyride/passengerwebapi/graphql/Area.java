package io.vividcode.happyride.passengerwebapi.graphql;

import java.math.BigDecimal;
import java.util.List;
import lombok.Data;

@Data
public class Area {

  private Integer id;

  private Integer level;

  private Long parentCode;

  private Long areaCode;

  private String zipCode;

  private String cityCode;

  private String name;

  private String shortName;

  private String mergerName;

  private String pinyin;

  private BigDecimal lat;

  private BigDecimal lng;

  private List<Area> ancestors;
}

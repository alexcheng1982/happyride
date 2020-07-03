package io.vividcode.happyride.passengerwebapi.graphql;

import java.math.BigDecimal;
import java.util.List;
import lombok.Data;

@Data
public class Address {

  private String id;

  private Integer areaId;

  private String addressLine;

  private BigDecimal lng;

  private BigDecimal lat;

  private List<Area> areas;
}

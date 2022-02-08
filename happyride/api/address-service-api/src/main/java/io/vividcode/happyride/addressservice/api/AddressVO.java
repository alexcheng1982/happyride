package io.vividcode.happyride.addressservice.api;

import java.math.BigDecimal;
import java.util.List;
import lombok.Data;

@Data
public class AddressVO {

  private String id;

  private Integer areaId;

  private String addressLine;

  private BigDecimal lng;

  private BigDecimal lat;

  private List<AreaVO> areas;

  public static AddressVO nullObject(String id) {
    AddressVO result = new AddressVO();
    result.setId(id);
    return result;
  }
}

package io.vividcode.happyride.passengerwebapi;

import io.vividcode.happyride.addressservice.api.AreaVO;
import java.math.BigDecimal;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserAddressResponse {

  private String id;

  private String name;

  private String addressId;

  private Integer areaId;

  private String addressLine;

  private BigDecimal lng;

  private BigDecimal lat;

  private List<AreaVO> areas;
}

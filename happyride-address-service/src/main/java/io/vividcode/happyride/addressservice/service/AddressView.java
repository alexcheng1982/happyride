package io.vividcode.happyride.addressservice.service;

import io.vividcode.happyride.addressservice.domain.Address;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import lombok.Data;

@Data
public class AddressView {

  private String id;

  private Integer areaId;

  private String addressLine;

  private BigDecimal lng;

  private BigDecimal lat;

  private List<AreaView> areas;

  public static AddressView fromAddress(final Address address) {
    return fromAddress(address, Collections.emptyList());
  }

  public static AddressView fromAddress(final Address address, final List<AreaView> areas) {
    final AddressView addressView = new AddressView();
    addressView.setId(address.getId());
    addressView.setAreaId(address.getArea().getId());
    addressView.setAddressLine(address.getAddressLine());
    addressView.setLng(address.getLng());
    addressView.setLat(address.getLat());
    addressView.setAreas(areas);
    return addressView;
  }
}

package io.vividcode.happyride.addressservice.service;

import io.vividcode.happyride.addressservice.domain.Address;
import java.math.BigDecimal;
import lombok.Data;

@Data
public class AddressView {
  private Integer areaId;

  private String addressLine;

  private BigDecimal lng;

  private BigDecimal lat;

  public static AddressView fromAddress(Address address) {
    AddressView addressView = new AddressView();
    addressView.setAreaId(address.getArea().getId());
    addressView.setAddressLine(address.getAddressLine());
    addressView.setLng(address.getLng());
    addressView.setLat(address.getLat());
    return addressView;
  }
}

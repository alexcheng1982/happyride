package io.vividcode.happyride.addressservice.web;

import java.math.BigDecimal;
import org.springframework.hateoas.RepresentationModel;

public class AddressModel extends RepresentationModel<AddressModel> {

  public String id;

  public Integer areaId;

  public Long areaCode;

  public String addressLine;

  public BigDecimal lng;

  public BigDecimal lat;
}

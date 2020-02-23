package io.vividcode.happyride.passengerservice.api.web;

import lombok.Data;
import lombok.NonNull;

@Data
public class UserAddress {

  @NonNull
  private String id;

  private String name;

  private String addressId;
}

package io.vividcode.happyride.passengerservice.api.web;

import lombok.Data;
import lombok.NonNull;

@Data
public class UserAddressVO {

  @NonNull
  private String id;

  @NonNull
  private String name;

  @NonNull
  private String addressId;
}

package io.vividcode.happyride.passengerservice.api.web;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserAddressVO {

  @NonNull
  private String id;

  @NonNull
  private String name;

  @NonNull
  private String addressId;
}

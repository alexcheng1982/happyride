package io.vividcode.happyride.passengerservice.api.web;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
public class UserAddressView {

  @NonNull
  private String id;

  @NonNull
  private String passengerId;

  @NonNull
  private String name;

  @NonNull
  private String addressId;
}

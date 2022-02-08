package io.vividcode.happyride.passengerservice.api.web;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@NoArgsConstructor
public class CreateUserAddressRequest {

  @NonNull
  private String name;

  @NonNull
  private String addressId;

}

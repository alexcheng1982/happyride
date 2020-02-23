package io.vividcode.happyride.passengerservice.api.web;

import lombok.Data;

@Data
public class CreateUserAddressRequest {

  private String name;

  private String addressId;
}

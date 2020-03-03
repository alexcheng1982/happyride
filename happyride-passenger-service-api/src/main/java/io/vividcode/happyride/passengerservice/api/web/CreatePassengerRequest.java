package io.vividcode.happyride.passengerservice.api.web;

import java.util.HashSet;
import java.util.Set;
import lombok.Data;

@Data
public class CreatePassengerRequest {

  private String name;

  private String email;

  private String mobilePhoneNumber;

  private Set<CreateUserAddressRequest> userAddresses = new HashSet<>();
}

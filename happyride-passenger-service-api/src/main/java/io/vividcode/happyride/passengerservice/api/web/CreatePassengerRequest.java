package io.vividcode.happyride.passengerservice.api.web;

import java.util.ArrayList;
import java.util.List;
import lombok.Data;

@Data
public class CreatePassengerRequest {

  private String name;

  private String email;

  private String mobilePhoneNumber;

  private List<CreateUserAddressRequest> userAddresses = new ArrayList<>();
}

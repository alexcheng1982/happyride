package io.vividcode.happyride.passengerservice.api.web;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PassengerView {
  private String id;

  private String name;

  private String email;

  private String mobilePhoneNumber;

  private List<UserAddressView> userAddresses;
}

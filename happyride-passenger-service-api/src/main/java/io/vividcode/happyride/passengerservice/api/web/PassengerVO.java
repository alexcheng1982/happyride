package io.vividcode.happyride.passengerservice.api.web;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

@Data
@AllArgsConstructor
public class PassengerVO {
  @NonNull
  private String id;

  @NonNull
  private String name;

  private String email;

  @NonNull
  private String mobilePhoneNumber;

  private List<UserAddressVO> userAddresses;
}

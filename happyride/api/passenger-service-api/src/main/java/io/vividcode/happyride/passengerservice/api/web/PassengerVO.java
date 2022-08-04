package io.vividcode.happyride.passengerservice.api.web;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
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

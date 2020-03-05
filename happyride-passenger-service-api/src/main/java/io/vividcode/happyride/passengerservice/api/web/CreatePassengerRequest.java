package io.vividcode.happyride.passengerservice.api.web;

import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@NoArgsConstructor
public class CreatePassengerRequest {

  @NonNull
  private String name;

  private String email;

  @NonNull
  private String mobilePhoneNumber;

  private List<CreateUserAddressRequest> userAddresses = new ArrayList<>();
}

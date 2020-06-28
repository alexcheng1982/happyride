package io.vividcode.happyride.passengerwebapi;

import io.vividcode.happyride.addressservice.api.AddressVO;
import io.vividcode.happyride.passengerservice.api.web.PassengerVO;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PassengerResponse {

  private String id;

  private String name;

  private String email;

  private String mobilePhoneNumber;

  private List<AddressVO> userAddresses;

  public static PassengerResponse fromPassengerAndAddresses(
      final PassengerVO passenger,
      final List<AddressVO> addresses) {
    return new PassengerResponse(
        passenger.getId(),
        passenger.getName(),
        passenger.getEmail(),
        passenger.getMobilePhoneNumber(),
        addresses
    );
  }
}

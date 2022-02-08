package io.vividcode.happyride.passengerwebapi.graphql;

import java.util.List;
import lombok.Data;

@Data
public class Passenger {

  private String id;

  private String name;

  private String email;

  private String mobilePhoneNumber;

  private List<UserAddress> userAddresses;
}

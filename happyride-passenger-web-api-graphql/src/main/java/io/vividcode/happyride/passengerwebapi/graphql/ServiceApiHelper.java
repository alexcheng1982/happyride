package io.vividcode.happyride.passengerwebapi.graphql;

import io.vividcode.happyride.passengerservice.api.web.PassengerVO;
import io.vividcode.happyride.passengerservice.api.web.UserAddressVO;
import java.util.stream.Collectors;

public class ServiceApiHelper {

  public static Passenger fromPassengerVO(PassengerVO passengerVO) {
    Passenger passenger = new Passenger();
    passenger.setId(passengerVO.getId());
    passenger.setName(passengerVO.getName());
    passenger.setEmail(passengerVO.getEmail());
    passenger.setMobilePhoneNumber(passengerVO.getMobilePhoneNumber());
    passenger.setUserAddresses(
        passengerVO.getUserAddresses().stream()
            .map(ServiceApiHelper::fromUserAddressVO)
            .collect(Collectors.toList()));
    return passenger;
  }

  public static UserAddress fromUserAddressVO(UserAddressVO userAddressVO) {
    UserAddress userAddress = new UserAddress();
    userAddress.setId(userAddressVO.getId());
    userAddress.setName(userAddressVO.getName());
    userAddress.setAddressId(userAddressVO.getAddressId());
    return userAddress;
  }
}

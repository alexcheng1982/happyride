package io.vividcode.happyride.passengerservice.domain;

import com.github.javafaker.Faker;
import io.vividcode.happyride.passengerservice.api.web.CreatePassengerRequest;
import io.vividcode.happyride.passengerservice.api.web.CreateUserAddressRequest;
import io.vividcode.happyride.passengerservice.api.web.PassengerVO;
import io.vividcode.happyride.passengerservice.api.web.UserAddressVO;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.stream.Collectors;

public class PassengerUtils {

  private static final Faker faker = new Faker(Locale.CHINA);

  public static CreatePassengerRequest buildCreatePassengerRequest(final int numberOfAddresses) {
    final CreatePassengerRequest request = new CreatePassengerRequest();
    request.setName(faker.name().name());
    request.setEmail(faker.internet().emailAddress());
    request.setMobilePhoneNumber(faker.phoneNumber().phoneNumber());
    final int count = Math.max(0, numberOfAddresses);
    final List<CreateUserAddressRequest> addresses = new ArrayList<>(count);
    for (int i = 0; i < count; i++) {
      addresses.add(buildCreateUserAddressRequest());
    }
    request.setUserAddresses(addresses);
    return request;
  }

  public static CreateUserAddressRequest buildCreateUserAddressRequest() {
    final CreateUserAddressRequest request = new CreateUserAddressRequest();
    request.setName(faker.pokemon().name());
    request.setAddressId(UUID.randomUUID().toString());
    return request;
  }

  public static PassengerVO createPassengerVO(final Passenger passenger) {
    return new PassengerVO(passenger.getId(),
        passenger.getName(),
        passenger.getEmail(),
        passenger.getMobilePhoneNumber(),
        passenger.getUserAddresses().stream().map(PassengerUtils::createUserAddressVO)
            .collect(Collectors.toList()));
  }

  public static UserAddressVO createUserAddressVO(final UserAddress userAddress) {
    return new UserAddressVO(userAddress.getId(),
        userAddress.getName(),
        userAddress.getAddressId());
  }
}

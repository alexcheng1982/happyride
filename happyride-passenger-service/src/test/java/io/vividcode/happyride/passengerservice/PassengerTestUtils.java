package io.vividcode.happyride.passengerservice;

import com.github.javafaker.Faker;
import io.vividcode.happyride.passengerservice.api.web.CreatePassengerRequest;
import io.vividcode.happyride.passengerservice.api.web.CreateUserAddressRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import org.springframework.stereotype.Component;

@Component
public class PassengerTestUtils {

  private final Faker faker = new Faker(Locale.CHINA);

  public CreatePassengerRequest buildCreatePassengerRequest(int numberOfAddresses) {
    CreatePassengerRequest request = new CreatePassengerRequest();
    request.setName(faker.name().name());
    request.setEmail(faker.internet().emailAddress());
    request.setMobilePhoneNumber(faker.phoneNumber().phoneNumber());
    int count = Math.max(0, numberOfAddresses);
    List<CreateUserAddressRequest> addresses = new ArrayList<>(count);
    for (int i = 0; i < count; i++) {
      addresses.add(buildCreateUserAddressRequest());
    }
    request.setUserAddresses(addresses);
    return request;
  }

  public CreateUserAddressRequest buildCreateUserAddressRequest() {
    CreateUserAddressRequest request = new CreateUserAddressRequest();
    request.setName(faker.pokemon().name());
    request.setAddressId(UUID.randomUUID().toString());
    return request;
  }
}

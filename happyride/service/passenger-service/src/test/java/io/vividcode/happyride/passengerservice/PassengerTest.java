package io.vividcode.happyride.passengerservice;

import static org.assertj.core.api.Assertions.assertThat;

import com.github.javafaker.Faker;
import io.vividcode.happyride.passengerservice.domain.Passenger;
import io.vividcode.happyride.passengerservice.domain.UserAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Passenger test")
public class PassengerTest {

  private static final Faker faker = new Faker(Locale.CHINA);

  @Test
  @DisplayName("Add user address")
  public void testAddUserAddress() {
    final Passenger passenger = this.createPassenger(1);
    passenger.addUserAddress(this.createUserAddress());
    assertThat(passenger.getUserAddresses()).hasSize(2);
  }

  @Test
  @DisplayName("Remove user address")
  public void testRemoveUserAddress() {
    final Passenger passenger = this.createPassenger(3);
    final String addressId = passenger.getUserAddresses().get(0).getId();
    passenger.removeUserAddress(addressId);
    assertThat(passenger.getUserAddresses()).hasSize(2);
  }

  @Test
  @DisplayName("Get user address")
  public void testGetUserAddress() {
    final Passenger passenger = this.createPassenger(2);
    final String addressId = passenger.getUserAddresses().get(0).getId();
    assertThat(passenger.getUserAddress(addressId)).isPresent();
    assertThat(passenger.getUserAddress("invalid")).isEmpty();
  }

  private Passenger createPassenger(final int numberOfAddresses) {
    final Passenger passenger = new Passenger();
    passenger.generateId();
    passenger.setName(faker.name().fullName());
    passenger.setEmail(faker.internet().emailAddress());
    passenger.setMobilePhoneNumber(faker.phoneNumber().phoneNumber());
    final int count = Math.max(0, numberOfAddresses);
    final List<UserAddress> addresses = new ArrayList<>(count);
    for (int i = 0; i < count; i++) {
      addresses.add(this.createUserAddress());
    }
    passenger.setUserAddresses(addresses);
    return passenger;
  }

  private UserAddress createUserAddress() {
    final UserAddress userAddress = new UserAddress();
    userAddress.generateId();
    userAddress.setName(faker.pokemon().name());
    userAddress.setAddressId(UUID.randomUUID().toString());
    return userAddress;
  }
}

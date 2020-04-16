package io.vividcode.happyride.passengerservice;

import static org.assertj.core.api.Assertions.assertThat;

import com.playtika.test.postgresql.EmbeddedPostgreSQLBootstrapConfiguration;
import com.playtika.test.postgresql.EmbeddedPostgreSQLDependenciesAutoConfiguration;
import io.vividcode.happyride.passengerservice.api.web.CreatePassengerRequest;
import io.vividcode.happyride.passengerservice.api.web.PassengerVO;
import io.vividcode.happyride.passengerservice.domain.PassengerService;
import io.vividcode.happyride.passengerservice.domain.PassengerUtils;
import io.vividcode.happyride.postgres.common.EmbeddedPostgresConfiguration;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;

@DataJpaTest
@EnableAutoConfiguration
@AutoConfigureTestDatabase(replace = Replace.NONE)
@ComponentScan
@ContextConfiguration(classes = {
    EmbeddedPostgresConfiguration.class
})
@ImportAutoConfiguration(classes = {
    EmbeddedPostgreSQLDependenciesAutoConfiguration.class,
    EmbeddedPostgreSQLBootstrapConfiguration.class
})
@TestPropertySource(properties = {
    "embedded.postgresql.docker-image=postgres:12-alpine"
})
@DisplayName("Passenger service test")
public class PassengerServiceTest {

  @Autowired
  PassengerService passengerService;

  @Test
  @DisplayName("Create a new passenger")
  public void testCreatePassenger() {
    CreatePassengerRequest request = PassengerUtils.buildCreatePassengerRequest(1);
    PassengerVO passenger = passengerService.createPassenger(request);
    assertThat(passenger.getId()).isNotNull();
    assertThat(passenger.getUserAddresses()).hasSize(1);
  }

  @Test
  @DisplayName("Add a user address")
  public void testAddAddress() {
    CreatePassengerRequest request = PassengerUtils.buildCreatePassengerRequest(1);
    PassengerVO passenger = passengerService.createPassenger(request);
    passenger = passengerService
        .addAddress(passenger.getId(), PassengerUtils.buildCreateUserAddressRequest());
    assertThat(passenger.getUserAddresses()).hasSize(2);
  }

  @Test
  @DisplayName("Delete a user address")
  public void testDeleteAddress() {
    CreatePassengerRequest request = PassengerUtils.buildCreatePassengerRequest(3);
    PassengerVO passenger = passengerService.createPassenger(request);
    String addressId = passenger.getUserAddresses().get(1).getId();
    passenger = passengerService.deleteAddress(passenger.getId(), addressId);
    assertThat(passenger.getUserAddresses()).hasSize(2);
  }
}

package io.vividcode.happyride.passengerservice;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;

import com.playtika.test.common.spring.EmbeddedContainersShutdownAutoConfiguration;
import com.playtika.test.postgresql.EmbeddedPostgreSQLBootstrapConfiguration;
import com.playtika.test.postgresql.EmbeddedPostgreSQLDependenciesAutoConfiguration;
import io.eventuate.tram.spring.consumer.jdbc.TramConsumerJdbcAutoConfiguration;
import io.vividcode.happyride.passengerservice.api.web.PassengerVO;
import io.vividcode.happyride.passengerservice.client.ApiClient;
import io.vividcode.happyride.passengerservice.client.ApiException;
import io.vividcode.happyride.passengerservice.client.ApiResponse;
import io.vividcode.happyride.passengerservice.client.Configuration;
import io.vividcode.happyride.passengerservice.client.api.PassengerApi;
import io.vividcode.happyride.passengerservice.domain.PassengerUtils;
import io.vividcode.happyride.postgres.common.EmbeddedPostgresConfiguration;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@EnableAutoConfiguration(exclude = {
    TramConsumerJdbcAutoConfiguration.class,
    SecurityAutoConfiguration.class})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(classes = {
    EmbeddedPostgresConfiguration.class,
    PassengerTestApplication.class
})
@ImportAutoConfiguration(classes = {
    EmbeddedPostgreSQLDependenciesAutoConfiguration.class,
    EmbeddedPostgreSQLBootstrapConfiguration.class,
    EmbeddedContainersShutdownAutoConfiguration.class
})
@TestPropertySource(properties = {
    "embedded.postgresql.docker-image=postgres:12-alpine"
})
@DisplayName("Passenger controller test")
public class PassengerControllerClientTest {

  private final PassengerApi passengerApi;

  public PassengerControllerClientTest(@LocalServerPort int serverPort) {
    ApiClient apiClient = Configuration.getDefaultApiClient();
    apiClient.setBasePath("http://localhost:" + serverPort);
    this.passengerApi = new PassengerApi(apiClient);
  }

  @Test
  @DisplayName("Create a new passenger")
  void testCreatePassenger() {
    try {
      ApiResponse<Void> response = this.passengerApi
          .createPassengerWithHttpInfo(
              PassengerUtils.buildCreatePassengerRequest(1));
      assertThat(response.getStatusCode()).isEqualTo(201);
      assertThat(response.getHeaders()).containsKey("Location");
    } catch (ApiException e) {
      fail(e);
    }
  }

  @Test
  @DisplayName("Add a new address")
  public void testAddUserAddress() {
    try {
      String passengerId = this.createPassenger(1);
      PassengerVO passenger = this.passengerApi
          .createAddress(passengerId, PassengerUtils.buildCreateUserAddressRequest());
      assertThat(passenger.getUserAddresses()).hasSize(2);
    } catch (ApiException e) {
      fail(e);
    }
  }

  @Test
  @DisplayName("Remove an address")
  public void testRemoveAddress() {
    try {
      String passengerId = this.createPassenger(3);
      PassengerVO passenger = this.passengerApi.getPassenger(passengerId);
      String addressId = passenger.getUserAddresses().get(0).getId();
      this.passengerApi.deleteAddress(passengerId, addressId);
      passenger = this.passengerApi.getPassenger(passengerId);
      assertThat(passenger.getUserAddresses()).hasSize(2);
    } catch (ApiException e) {
      fail(e);
    }
  }

  private String createPassenger(int numberOfAddresses) throws ApiException {
    ApiResponse<Void> response = this.passengerApi
        .createPassengerWithHttpInfo(
            PassengerUtils.buildCreatePassengerRequest(numberOfAddresses));
    assertThat(response.getHeaders()).containsKey("Location");
    String location = response.getHeaders().get("Location").get(0);
    return StringUtils.substringAfterLast(location, "/");
  }
}

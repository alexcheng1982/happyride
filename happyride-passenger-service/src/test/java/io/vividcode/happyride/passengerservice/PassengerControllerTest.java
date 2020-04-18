package io.vividcode.happyride.passengerservice;

import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.hasSize;

import com.playtika.test.postgresql.EmbeddedPostgreSQLBootstrapConfiguration;
import com.playtika.test.postgresql.EmbeddedPostgreSQLDependenciesAutoConfiguration;
import io.vividcode.happyride.passengerservice.api.web.PassengerVO;
import io.vividcode.happyride.passengerservice.domain.PassengerUtils;
import io.vividcode.happyride.postgres.common.EmbeddedPostgresConfiguration;
import java.net.URI;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@EnableAutoConfiguration
@ComponentScan
@Import(EmbeddedPostgresConfiguration.class)
@ImportAutoConfiguration(classes = {
    EmbeddedPostgreSQLDependenciesAutoConfiguration.class,
    EmbeddedPostgreSQLBootstrapConfiguration.class
})
@TestPropertySource(properties = {
    "embedded.postgresql.docker-image=postgres:12-alpine"
})
@DisplayName("Passenger controller test")
public class PassengerControllerTest {

  private final String baseUri = "/api/v1";

  @Autowired
  WebTestClient webClient;

  @Autowired
  TestRestTemplate restTemplate;

  @Test
  @DisplayName("Create a new passenger")
  public void testCreatePassenger() {
    webClient.post()
        .uri(baseUri)
        .bodyValue(PassengerUtils.buildCreatePassengerRequest(1))
        .accept(MediaType.APPLICATION_JSON)
        .exchange()
        .expectStatus().isCreated()
        .expectHeader().exists(HttpHeaders.LOCATION);
  }

  @Test
  @DisplayName("Add a new address")
  public void testAddUserAddress() {
    URI passengerUri = restTemplate
        .postForLocation(baseUri,
            PassengerUtils.buildCreatePassengerRequest(1));
    URI addressesUri = ServletUriComponentsBuilder.fromUri(passengerUri)
        .path("/addresses").build().toUri();
    webClient.post().uri(addressesUri)
        .bodyValue(PassengerUtils.buildCreateUserAddressRequest())
        .exchange()
        .expectStatus().isOk()
        .expectBody(PassengerVO.class)
        .value(hasProperty("userAddresses", hasSize(2)));
  }

  @Test
  @DisplayName("Remove an address")
  public void testRemoveAddress() {
    URI passengerUri = restTemplate
        .postForLocation(baseUri,
            PassengerUtils.buildCreatePassengerRequest(3));
    PassengerVO passenger = restTemplate
        .getForObject(passengerUri, PassengerVO.class);
    String addressId = passenger.getUserAddresses().get(0).getId();
    URI addressUri = ServletUriComponentsBuilder.fromUri(passengerUri)
        .path("/addresses/" + addressId).build().toUri();
    webClient.delete().uri(addressUri)
        .exchange()
        .expectStatus().isNoContent();
    webClient.get().uri(passengerUri)
        .exchange()
        .expectStatus().isOk()
        .expectBody(PassengerVO.class)
        .value(hasProperty("userAddresses", hasSize(2)));
  }

}

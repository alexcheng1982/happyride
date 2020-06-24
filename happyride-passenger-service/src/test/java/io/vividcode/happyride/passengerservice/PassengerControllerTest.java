package io.vividcode.happyride.passengerservice;

import com.playtika.test.postgresql.EmbeddedPostgreSQLBootstrapConfiguration;
import com.playtika.test.postgresql.EmbeddedPostgreSQLDependenciesAutoConfiguration;
import io.eventuate.tram.spring.consumer.jdbc.TramConsumerJdbcAutoConfiguration;
import io.vividcode.happyride.passengerservice.api.web.PassengerVO;
import io.vividcode.happyride.passengerservice.domain.PassengerUtils;
import io.vividcode.happyride.postgres.common.EmbeddedPostgresConfiguration;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.hasSize;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@EnableAutoConfiguration(exclude = {TramConsumerJdbcAutoConfiguration.class, SecurityAutoConfiguration.class})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(classes = {
    EmbeddedPostgresConfiguration.class,
    PassengerTestApplication.class
})
@ImportAutoConfiguration(classes = {
    EmbeddedPostgreSQLDependenciesAutoConfiguration.class,
    EmbeddedPostgreSQLBootstrapConfiguration.class
})
@TestPropertySource(properties = {
    "embedded.postgresql.docker-image=postgres:12-alpine"
})
@DisplayName("Passenger controller test")
public class PassengerControllerTest {

  private final String baseUri = "/";

  @Autowired
  WebTestClient webClient;

  @Autowired
  TestRestTemplate restTemplate;

  @Test
  @DisplayName("Create a new passenger")
  public void testCreatePassenger() {
    this.webClient.post()
        .uri(this.baseUri)
        .bodyValue(PassengerUtils.buildCreatePassengerRequest(1))
        .accept(MediaType.APPLICATION_JSON)
        .exchange()
        .expectStatus().isCreated()
        .expectHeader().exists(HttpHeaders.LOCATION);
  }

  @Test
  @DisplayName("Add a new address")
  public void testAddUserAddress() {
    final URI passengerUri = this.restTemplate
        .postForLocation(this.baseUri,
            PassengerUtils.buildCreatePassengerRequest(1));
    final URI addressesUri = ServletUriComponentsBuilder.fromUri(passengerUri)
        .path("/addresses").build().toUri();
    this.webClient.post().uri(addressesUri)
        .bodyValue(PassengerUtils.buildCreateUserAddressRequest())
        .exchange()
        .expectStatus().isOk()
        .expectBody(PassengerVO.class)
        .value(hasProperty("userAddresses", hasSize(2)));
  }

  @Test
  @DisplayName("Remove an address")
  public void testRemoveAddress() {
    final URI passengerUri = this.restTemplate
        .postForLocation(this.baseUri,
            PassengerUtils.buildCreatePassengerRequest(3));
    final PassengerVO passenger = this.restTemplate
        .getForObject(passengerUri, PassengerVO.class);
    final String addressId = passenger.getUserAddresses().get(0).getId();
    final URI addressUri = ServletUriComponentsBuilder.fromUri(passengerUri)
        .path("/addresses/" + addressId).build().toUri();
    this.webClient.delete().uri(addressUri)
        .exchange()
        .expectStatus().isNoContent();
    this.webClient.get().uri(passengerUri)
        .exchange()
        .expectStatus().isOk()
        .expectBody(PassengerVO.class)
        .value(hasProperty("userAddresses", hasSize(2)));
  }

}

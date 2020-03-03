package io.vividcode.happyride.passengerservice.web;

import com.playtika.test.postgresql.EmbeddedPostgreSQLBootstrapConfiguration;
import com.playtika.test.postgresql.EmbeddedPostgreSQLDependenciesAutoConfiguration;
import io.vividcode.happyride.passengerservice.api.web.CreatePassengerRequest;
import io.vividcode.happyride.passengerservice.api.web.CreateUserAddressRequest;
import io.vividcode.happyride.postgres.common.EmbeddedPostgresConfiguration;
import java.net.URI;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@EnableAutoConfiguration
@ComponentScan
@Import({
    EmbeddedPostgresConfiguration.class}
)
@ImportAutoConfiguration(classes = {EmbeddedPostgreSQLDependenciesAutoConfiguration.class,
    EmbeddedPostgreSQLBootstrapConfiguration.class})
public class PassengerControllerTest {

  @Test
  public void testCreatePassenger(@Autowired WebTestClient webClient) {
    CreatePassengerRequest request = createPassengerRequest();
    webClient.post()
        .bodyValue(request)
        .accept(MediaType.APPLICATION_JSON)
        .exchange()
        .expectStatus().isCreated()
        .expectHeader().exists("Location");
  }

  @Test
  public void testAddUserAddress(@Autowired WebTestClient webClient, @Autowired TestRestTemplate restTemplate) {
    CreatePassengerRequest request = createPassengerRequest();
    CreateUserAddressRequest userAddressRequest = new CreateUserAddressRequest();
    userAddressRequest.setName("test");
    URI uri = restTemplate.postForLocation("/", request);
    webClient.post().uri(URI.create(uri.toString() + "/addresses"))
        .bodyValue(userAddressRequest)
        .exchange()
        .expectStatus().isCreated()
        .expectHeader().exists("Location");
  }

  private CreatePassengerRequest createPassengerRequest() {
    CreatePassengerRequest request = new CreatePassengerRequest();
    request.setName("test1");
    request.setEmail("test1@test.com");
    request.setMobilePhoneNumber("13400000000");
    return request;
  }
}

package io.vividcode.happyride.passengerservice;

import com.playtika.test.postgresql.EmbeddedPostgreSQLBootstrapConfiguration;
import com.playtika.test.postgresql.EmbeddedPostgreSQLDependenciesAutoConfiguration;
import io.vividcode.happyride.passengerservice.api.web.UserAddressView;
import io.vividcode.happyride.passengerservice.support.PassengerUtils;
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
@DisplayName("乘客控制器测试")
public class PassengerControllerTest {

  @Autowired
  PassengerUtils testUtils;

  private String baseUri = "/api/v1";

  @Test
  @DisplayName("创建乘客")
  public void testCreatePassenger(@Autowired WebTestClient webClient) {
    webClient.post()
        .uri(baseUri)
        .bodyValue(testUtils.buildCreatePassengerRequest(1))
        .accept(MediaType.APPLICATION_JSON)
        .exchange()
        .expectStatus().isCreated()
        .expectHeader().exists(HttpHeaders.LOCATION);
  }

  @Test
  @DisplayName("添加地址")
  public void testAddUserAddress(@Autowired WebTestClient webClient,
      @Autowired TestRestTemplate restTemplate) {
    URI passengerUri = restTemplate.postForLocation(baseUri, testUtils.buildCreatePassengerRequest(1));
    URI addressesUri = ServletUriComponentsBuilder.fromUri(passengerUri)
        .path("/addresses").build().toUri();
    webClient.post().uri(addressesUri)
        .bodyValue(testUtils.buildCreateUserAddressRequest())
        .exchange()
        .expectStatus().isCreated()
        .expectHeader().exists(HttpHeaders.LOCATION);
  }

  @Test
  @DisplayName("删除地址")
  public void testRemoveAddress(@Autowired WebTestClient webClient,
      @Autowired TestRestTemplate restTemplate) {
    URI passengerUri = restTemplate.postForLocation(baseUri, testUtils.buildCreatePassengerRequest(0));
    URI addressesUri = ServletUriComponentsBuilder.fromUri(passengerUri)
        .path("/addresses").build().toUri();
    URI addressUri = restTemplate
        .postForLocation(addressesUri, testUtils.buildCreateUserAddressRequest());
    restTemplate.postForLocation(addressesUri, testUtils.buildCreateUserAddressRequest());
    restTemplate.postForLocation(addressesUri, testUtils.buildCreateUserAddressRequest());
    webClient.delete().uri(addressUri)
        .exchange()
        .expectStatus().isNoContent();
    webClient.get().uri(addressesUri)
        .exchange()
        .expectStatus().isOk()
        .expectBodyList(UserAddressView.class)
        .hasSize(2);
  }

}

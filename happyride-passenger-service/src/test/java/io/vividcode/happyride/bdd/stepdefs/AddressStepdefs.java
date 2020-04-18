package io.vividcode.happyride.bdd.stepdefs;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;

import com.playtika.test.postgresql.EmbeddedPostgreSQLBootstrapConfiguration;
import com.playtika.test.postgresql.EmbeddedPostgreSQLDependenciesAutoConfiguration;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.vividcode.happyride.bdd.BddTestApplication;
import io.vividcode.happyride.bdd.common.PassengerClient;
import io.vividcode.happyride.passengerservice.PassengerServiceApplication;
import io.vividcode.happyride.passengerservice.api.web.PassengerVO;
import io.vividcode.happyride.passengerservice.client.ApiException;
import io.vividcode.happyride.postgres.common.EmbeddedPostgresConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@EnableAutoConfiguration
@ContextConfiguration(classes = {
    BddTestApplication.class,
    PassengerServiceApplication.class
})
@ComponentScan
@Import(EmbeddedPostgresConfiguration.class)
@ImportAutoConfiguration(classes = {
    EmbeddedPostgreSQLDependenciesAutoConfiguration.class,
    EmbeddedPostgreSQLBootstrapConfiguration.class
})
@TestPropertySource(properties = {
    "embedded.postgresql.docker-image=postgres:12-alpine"
})
public class AddressStepdefs {

  @Autowired
  PassengerClient passengerClient;

  private String passengerId;

  @Given("a passenger with {int} addresses")
  public void passengerWithAddresses(int numberOfAddresses) {
    try {
      passengerId = passengerClient.createPassenger(numberOfAddresses);
    } catch (ApiException e) {
      fail(e);
    }
  }

  @When("the passenger adds a new address")
  public void passengerAddsAddress() {
    try {
      passengerClient.addAddress(passengerId);
    } catch (ApiException e) {
      fail(e);
    }
  }

  @When("the passenger deletes an address")
  public void passengerDeletesAddress() {
    try {
      passengerClient.removeAddress(passengerId);
    } catch (ApiException e) {
      fail(e);
    }
  }

  @Then("the passenger has {int} addresses")
  public void passengerHasAddresses(int numberOfAddresses) {
    try {
      PassengerVO passenger = passengerClient.getPassenger(passengerId);
      assertThat(passenger.getUserAddresses()).hasSize(numberOfAddresses);
    } catch (ApiException e) {
      fail(e);
    }
  }

}

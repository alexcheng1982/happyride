package io.vividcode.happyride.bdd.stepdefs;

import com.playtika.test.postgresql.EmbeddedPostgreSQLBootstrapConfiguration;
import com.playtika.test.postgresql.EmbeddedPostgreSQLDependenciesAutoConfiguration;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.eventuate.tram.spring.consumer.jdbc.TramConsumerJdbcAutoConfiguration;
import io.vividcode.happyride.bdd.BddTestApplication;
import io.vividcode.happyride.bdd.common.PassengerClient;
import io.vividcode.happyride.passengerservice.PassengerTestApplication;
import io.vividcode.happyride.passengerservice.api.web.PassengerVO;
import io.vividcode.happyride.passengerservice.client.ApiException;
import io.vividcode.happyride.postgres.common.EmbeddedPostgresConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@EnableAutoConfiguration(exclude = {TramConsumerJdbcAutoConfiguration.class, SecurityAutoConfiguration.class})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(classes = {
    EmbeddedPostgresConfiguration.class,
    BddTestApplication.class,
    PassengerTestApplication.class
})
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
  public void passengerWithAddresses(final int numberOfAddresses) {
    try {
      this.passengerId = this.passengerClient.createPassenger(numberOfAddresses);
    } catch (final ApiException e) {
      fail(e);
    }
  }

  @When("the passenger adds a new address")
  public void passengerAddsAddress() {
    try {
      this.passengerClient.addAddress(this.passengerId);
    } catch (final ApiException e) {
      fail(e);
    }
  }

  @When("the passenger deletes an address")
  public void passengerDeletesAddress() {
    try {
      this.passengerClient.removeAddress(this.passengerId);
    } catch (final ApiException e) {
      fail(e);
    }
  }

  @Then("the passenger has {int} addresses")
  public void passengerHasAddresses(final int numberOfAddresses) {
    try {
      final PassengerVO passenger = this.passengerClient.getPassenger(this.passengerId);
      assertThat(passenger.getUserAddresses()).hasSize(numberOfAddresses);
    } catch (final ApiException e) {
      fail(e);
    }
  }

}

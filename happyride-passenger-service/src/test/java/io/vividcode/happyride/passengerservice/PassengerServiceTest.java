package io.vividcode.happyride.passengerservice;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

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
    EmbeddedPostgresConfiguration.class}
)
@ImportAutoConfiguration(classes = {
    EmbeddedPostgreSQLDependenciesAutoConfiguration.class,
    EmbeddedPostgreSQLBootstrapConfiguration.class
})
@TestPropertySource(properties = {
    "embedded.postgresql.docker-image=postgres:12-alpine"
})
@DisplayName("乘客服务测试")
public class PassengerServiceTest {

  @Autowired
  PassengerService passengerService;

  @Test
  @DisplayName("创建乘客")
  public void testCreatePassenger() {
    CreatePassengerRequest request = PassengerUtils.buildCreatePassengerRequest(1);
    PassengerVO passenger = passengerService.createPassenger(request);
    assertNotNull(passenger.getId());
    assertThat(passenger.getUserAddresses()).hasSize(1);
  }

  @Test
  @DisplayName("添加地址到已有乘客")
  public void testAddAddress() {
    CreatePassengerRequest request = PassengerUtils.buildCreatePassengerRequest(1);
    PassengerVO passenger = passengerService.createPassenger(request);
    passenger = passengerService.addAddress(passenger.getId(), PassengerUtils.buildCreateUserAddressRequest());
    assertThat(passenger.getUserAddresses()).hasSize(2);
  }

  @Test
  @DisplayName("删除已有地址")
  public void testDeleteAddress() {
    CreatePassengerRequest request = PassengerUtils.buildCreatePassengerRequest(3);
    PassengerVO passenger = passengerService.createPassenger(request);
    String addressId = passenger.getUserAddresses().get(1).getId();
    passenger = passengerService.deleteAddress(passenger.getId(), addressId);
    assertThat(passenger.getUserAddresses()).hasSize(2);
  }
}

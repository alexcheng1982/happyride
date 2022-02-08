package io.vividcode.happyride.bdd.common;

import static io.cucumber.spring.CucumberTestContext.SCOPE_CUCUMBER_GLUE;

import io.vividcode.happyride.passengerservice.api.web.PassengerVO;
import io.vividcode.happyride.passengerservice.client.ApiClient;
import io.vividcode.happyride.passengerservice.client.ApiException;
import io.vividcode.happyride.passengerservice.client.ApiResponse;
import io.vividcode.happyride.passengerservice.client.Configuration;
import io.vividcode.happyride.passengerservice.client.api.PassengerApi;
import io.vividcode.happyride.passengerservice.domain.PassengerUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(SCOPE_CUCUMBER_GLUE)
public class PassengerClient {

  private final PassengerApi passengerApi;

  public PassengerClient(@LocalServerPort final int serverPort) {
    final ApiClient apiClient = Configuration.getDefaultApiClient();
    apiClient.setBasePath("http://localhost:" + serverPort);
    this.passengerApi = new PassengerApi(apiClient);
  }

  public PassengerVO getPassenger(final String passengerId) throws ApiException {
    return this.passengerApi.getPassenger(passengerId);
  }

  public String createPassenger(final int numberOfAddresses) throws ApiException {
    final ApiResponse<Void> response = this.passengerApi
        .createPassengerWithHttpInfo(
            PassengerUtils.buildCreatePassengerRequest(numberOfAddresses));
    final String location = response.getHeaders().get("Location").get(0);
    return StringUtils.substringAfterLast(location, "/");
  }

  public void addAddress(final String passengerId) throws ApiException {
    this.passengerApi
        .createAddress(passengerId, PassengerUtils.buildCreateUserAddressRequest());
  }

  public void removeAddress(final String passengerId) throws ApiException {
    final PassengerVO passenger = this.passengerApi.getPassenger(passengerId);
    if (!passenger.getUserAddresses().isEmpty()) {
      final String addressId = passenger.getUserAddresses().get(0).getId();
      this.passengerApi.deleteAddress(passengerId, addressId);
    }
  }
}

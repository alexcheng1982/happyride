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

  public PassengerClient(@LocalServerPort int serverPort) {
    ApiClient apiClient = Configuration.getDefaultApiClient();
    apiClient.setBasePath("http://localhost:" + serverPort);
    passengerApi = new PassengerApi(apiClient);
  }

  public PassengerVO getPassenger(String passengerId) throws ApiException {
    return passengerApi.getPassenger(passengerId);
  }

  public String createPassenger(int numberOfAddresses) throws ApiException {
    ApiResponse<Void> response = passengerApi
        .createPassengerWithHttpInfo(
            PassengerUtils.buildCreatePassengerRequest(numberOfAddresses));
    String location = response.getHeaders().get("Location").get(0);
    return StringUtils.substringAfterLast(location, "/");
  }

  public void addAddress(String passengerId) throws ApiException {
    passengerApi
        .createAddress(PassengerUtils.buildCreateUserAddressRequest(),
            passengerId);
  }

  public void removeAddress(String passengerId) throws ApiException {
    PassengerVO passenger = passengerApi.getPassenger(passengerId);
    if (!passenger.getUserAddresses().isEmpty()) {
      String addressId = passenger.getUserAddresses().get(0).getId();
      passengerApi.deleteAddress(passengerId, addressId);
    }
  }
}

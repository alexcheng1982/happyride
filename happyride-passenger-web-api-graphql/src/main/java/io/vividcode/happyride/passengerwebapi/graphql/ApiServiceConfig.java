package io.vividcode.happyride.passengerwebapi.graphql;

import io.vividcode.happyride.addressservice.client.api.AddressApi;
import io.vividcode.happyride.passengerservice.client.api.PassengerApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(ServiceDestinationConfig.class)
public class ApiServiceConfig {

  @Autowired
  ServiceDestinationConfig config;

  @Bean
  public PassengerApi passengerApi() {
    final PassengerApi passengerApi = new PassengerApi();
    passengerApi.getApiClient().setBasePath(this.config.getPassenger());
    return passengerApi;
  }

  @Bean
  public AddressApi addressApi() {
    final AddressApi addressApi = new AddressApi();
    addressApi.getApiClient().setBasePath(this.config.getAddress());
    return addressApi;
  }
}

package io.vividcode.happyride.passengerwebapi.graphql;

import io.opentracing.Tracer;
import io.opentracing.contrib.concurrent.TracedExecutorService;
import io.vividcode.happyride.addressservice.client.api.AddressApi;
import io.vividcode.happyride.passengerservice.client.api.PassengerApi;
import okhttp3.Dispatcher;
import okhttp3.OkHttpClient;
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
  public PassengerApi passengerApi(Tracer tracer) {
    PassengerApi passengerApi = new PassengerApi();
    passengerApi.getApiClient().setHttpClient(
        this.updateHttpClient(tracer,
            passengerApi.getApiClient().getHttpClient()));
    passengerApi.getApiClient().setBasePath(this.config.getPassenger());
    return passengerApi;
  }

  @Bean
  public AddressApi addressApi(Tracer tracer) {
    AddressApi addressApi = new AddressApi();
    addressApi.getApiClient().setHttpClient(
        this.updateHttpClient(tracer,
            addressApi.getApiClient().getHttpClient()));
    addressApi.getApiClient().setBasePath(this.config.getAddress());
    return addressApi;
  }

  private OkHttpClient updateHttpClient(Tracer tracer,
      OkHttpClient httpClient) {
    return httpClient.newBuilder()
        .dispatcher(new Dispatcher(
            new TracedExecutorService(httpClient.dispatcher().executorService(),
                tracer)))
        .addInterceptor(new ApiTracingInterceptor(tracer)).build();
  }
}

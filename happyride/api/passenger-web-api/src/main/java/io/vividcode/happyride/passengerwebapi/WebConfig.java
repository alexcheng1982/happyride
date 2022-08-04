package io.vividcode.happyride.passengerwebapi;

import io.vividcode.happyride.passengerservice.api.web.PassengerVO;
import io.vividcode.happyride.passengerservice.api.web.UserAddressVO;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.config.EnableWebFlux;
import org.springframework.web.reactive.config.WebFluxConfigurer;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;

@Configuration
@EnableWebFlux
public class WebConfig implements WebFluxConfigurer {

  @Autowired
  PassengerHandler passengerHandler;

  @Autowired
  AddressServiceProxy addressServiceProxy;

  @Autowired
  DestinationConfig destinationConfig;

  @Bean
  public RouterFunction<?> getPassenger() {
    return RouterFunctions.route()
        .GET("/passengerX/{passengerId}", this.passengerHandler::getPassenger)
        .build();
  }

  @Bean
  public RouteLocator routes(final RouteLocatorBuilder builder) {
    return builder.routes()
        .route("enrich_passenger", r -> r.path("/passenger/{passengerId}")
            .filters(f -> f
                .stripPrefix(1)
                .modifyResponseBody(PassengerVO.class, PassengerResponse.class,
                    (exchange, passenger) -> {
                      final List<String> addressIds = passenger
                          .getUserAddresses()
                          .stream()
                          .map(UserAddressVO::getAddressId)
                          .collect(Collectors.toList());
                      return this.addressServiceProxy.getAddresses(addressIds)
                          .map(addresses ->
                              PassengerResponse
                                  .fromPassengerAndAddresses(passenger,
                                      addresses));
                    }))
            .uri(this.destinationConfig.getPassenger())).build();
  }
}

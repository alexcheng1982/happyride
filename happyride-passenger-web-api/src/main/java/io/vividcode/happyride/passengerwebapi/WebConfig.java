package io.vividcode.happyride.passengerwebapi;

import org.springframework.beans.factory.annotation.Autowired;
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

  @Bean
  public RouterFunction<?> getPassenger() {
    return RouterFunctions.route()
        .GET("/{passengerId}", this.passengerHandler::getPassenger)
        .build();
  }
}

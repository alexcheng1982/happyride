package io.vividcode.happyride.passengerwebapi;

import com.google.common.collect.ImmutableMap;
import io.vividcode.happyride.passengerservice.api.web.PassengerVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

@Service
public class PassengerServiceProxy {

  @Autowired
  DestinationConfig destinationConfig;

  public Mono<PassengerVO> getPassenger(final String passengerId) {
    return WebClient.create(this.destinationConfig.getPassenger())
        .get()
        .uri(uriBuilder -> uriBuilder.path("/{passengerId}")
            .build(ImmutableMap.of("passengerId", passengerId)))
        .retrieve()
        .bodyToMono(PassengerVO.class)
        .onErrorResume(WebClientResponseException.class, ex ->
            ex.getStatusCode() == HttpStatus.NOT_FOUND ? Mono.empty()
                : Mono.error(ex)
        );
  }
}

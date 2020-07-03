package io.vividcode.happyride.passengerwebapi;

import static org.springframework.web.reactive.function.server.ServerResponse.ok;

import io.vividcode.happyride.passengerservice.api.web.UserAddressVO;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Service
public class PassengerHandler {

  @Autowired
  AddressServiceProxy addressServiceProxy;

  @Autowired
  PassengerServiceProxy passengerServiceProxy;

  public Mono<ServerResponse> getPassenger(final ServerRequest request) {
    final String passengerId = request.pathVariable("passengerId");
    return this.passengerServiceProxy.getPassenger(passengerId)
        .flatMap(passenger -> {
          final List<String> addressIds = passenger.getUserAddresses()
              .stream()
              .map(UserAddressVO::getAddressId)
              .collect(Collectors.toList());
          return this.addressServiceProxy.getAddresses(addressIds)
              .map(addresses -> PassengerResponse
                  .fromPassengerAndAddresses(passenger, addresses));
        })
        .flatMap(
            passengerResponse -> ok().contentType(MediaType.APPLICATION_JSON)
                .bodyValue(passengerResponse))
        .switchIfEmpty(ServerResponse.notFound().build());
  }
}

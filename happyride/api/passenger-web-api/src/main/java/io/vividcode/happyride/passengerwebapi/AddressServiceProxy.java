package io.vividcode.happyride.passengerwebapi;

import com.google.common.collect.ImmutableMap;
import io.vividcode.happyride.addressservice.api.AddressVO;
import io.vividcode.happyride.addressservice.api.AreaVO;
import io.vividcode.happyride.addressservice.api.web.AddressBatchRequest;
import java.util.Collections;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

@Service
public class AddressServiceProxy {

  @Autowired
  DestinationConfig destinationConfig;

  public Mono<List<AddressVO>> search(final Long areaCode, final String query) {
    return WebClient.create(this.destinationConfig.getAddress())
        .get()
        .uri(uriBuilder -> uriBuilder.path("/search")
            .queryParam("areaCode", areaCode)
            .queryParam("query", query)
            .build())
        .retrieve()
        .bodyToMono(new ParameterizedTypeReference<List<AddressVO>>() {
        })
        .onErrorReturn(Collections.emptyList());
  }

  public Mono<AddressVO> getAddress(final String addressId,
      final int areaLevel) {
    return WebClient.create(this.destinationConfig.getAddress())
        .get()
        .uri(uriBuilder -> uriBuilder.path("/address/{addressId}")
            .queryParam("areaLevel", areaLevel)
            .build(ImmutableMap.of("addressId", addressId)))
        .retrieve()
        .bodyToMono(AddressVO.class)
        .onErrorResume(WebClientResponseException.class, ex ->
            ex.getStatusCode() == HttpStatus.NOT_FOUND ? Mono.empty()
                : Mono.error(ex)
        );
  }

  public Mono<List<AddressVO>> getAddresses(final List<String> addressIds) {
    return WebClient.create(this.destinationConfig.getAddress())
        .post()
        .uri(uriBuilder -> uriBuilder.path("/addresses").build())
        .bodyValue(new AddressBatchRequest(addressIds))
        .retrieve()
        .bodyToMono(new ParameterizedTypeReference<List<AddressVO>>() {
        })
        .onErrorReturn(Collections.emptyList());
  }

  public Mono<AreaVO> getArea(final Long areaCode, final int ancestorLevel) {
    return WebClient.create(this.destinationConfig.getAddress())
        .get()
        .uri(uriBuilder -> uriBuilder.path("/area/{areaCode}")
            .queryParam("ancestorLevel", ancestorLevel)
            .build(ImmutableMap.of("areaCode", areaCode)))
        .retrieve()
        .bodyToMono(AreaVO.class)
        .onErrorResume(WebClientResponseException.class, ex ->
            ex.getStatusCode() == HttpStatus.NOT_FOUND ? Mono.empty()
                : Mono.error(ex)
        );
  }
}

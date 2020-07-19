package io.vividcode.happyride.passengerwebapi.graphql;

import graphql.kickstart.tools.GraphQLQueryResolver;
import io.vividcode.happyride.addressservice.api.AddressVO;
import io.vividcode.happyride.addressservice.client.api.AddressApi;
import io.vividcode.happyride.passengerservice.client.ApiException;
import io.vividcode.happyride.passengerservice.client.api.PassengerApi;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Query implements GraphQLQueryResolver {

  @Autowired
  PassengerApi passengerApi;

  @Autowired
  AddressApi addressApi;

  public List<Passenger> passengers(int page, int size)
      throws ApiException {
    return this.passengerApi.listPassengers(page, size).stream()
        .map(ServiceApiHelper::fromPassengerVO)
        .collect(Collectors.toList());
  }

  public Passenger passenger(String id) throws ApiException {
    return Optional.ofNullable(this.passengerApi.getPassenger(id))
        .map(ServiceApiHelper::fromPassengerVO)
        .orElse(null);
  }

  public AddressVO address(String id, int areaLevel)
      throws io.vividcode.happyride.addressservice.client.ApiException {
    return this.addressApi.getAddress(id, areaLevel);
  }

  public List<AddressVO> searchAddress(String areaCode,
      String query)
      throws io.vividcode.happyride.addressservice.client.ApiException {
    return this.addressApi.searchAddress(Long.valueOf(areaCode), query);
  }

}

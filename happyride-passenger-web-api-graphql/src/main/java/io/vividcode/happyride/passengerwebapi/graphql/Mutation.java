package io.vividcode.happyride.passengerwebapi.graphql;

import graphql.kickstart.tools.GraphQLMutationResolver;
import io.vividcode.happyride.passengerservice.api.web.CreateUserAddressRequest;
import io.vividcode.happyride.passengerservice.client.ApiException;
import io.vividcode.happyride.passengerservice.client.api.PassengerApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Mutation implements GraphQLMutationResolver {

  @Autowired
  PassengerApi passengerApi;

  public Passenger addUserAddress(final String passengerId,
      final CreateUserAddressRequest request)
      throws ApiException {
    return ServiceApiHelper
        .fromPassengerVO(this.passengerApi.createAddress(passengerId, request));
  }
}

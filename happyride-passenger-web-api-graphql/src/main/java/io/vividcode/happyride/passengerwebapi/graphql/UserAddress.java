package io.vividcode.happyride.passengerwebapi.graphql;

import graphql.kickstart.execution.context.GraphQLContext;
import graphql.schema.DataFetchingEnvironment;
import io.vividcode.happyride.addressservice.api.AddressVO;
import java.util.concurrent.CompletableFuture;
import lombok.Data;

@Data
public class UserAddress {

  private String id;
  private String name;
  private String addressId;

  public CompletableFuture<AddressVO> getAddress(
      final DataFetchingEnvironment environment) {
    final GraphQLContext context = environment.getContext();
    return context.getDataLoaderRegistry()
        .map(
            registry -> registry.<String, AddressVO>getDataLoader("userAddress")
                .load(this.addressId))
        .orElse(CompletableFuture.completedFuture(null));
  }
}

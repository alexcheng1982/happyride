package io.vividcode.happyride.passengerwebapi.graphql;

import static io.vividcode.happyride.passengerwebapi.graphql.SchemaConfig.USER_ADDRESS_DATA_LOADER;

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
      DataFetchingEnvironment environment) {
    GraphQLContext context = environment.getContext();
    return context.getDataLoaderRegistry()
        .map(
            registry -> registry.
                <String, AddressVO>getDataLoader(USER_ADDRESS_DATA_LOADER)
                .load(this.addressId))
        .orElse(CompletableFuture
            .completedFuture(AddressVO.nullObject(this.addressId)));
  }
}

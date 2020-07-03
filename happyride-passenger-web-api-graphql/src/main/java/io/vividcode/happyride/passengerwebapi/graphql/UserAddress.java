package io.vividcode.happyride.passengerwebapi.graphql;

import graphql.schema.DataFetchingEnvironment;
import io.vividcode.happyride.addressservice.api.AddressVO;
import lombok.Data;

@Data
public class UserAddress {

  private String id;
  private String name;

  public AddressVO getAddress(final DataFetchingEnvironment environment) {
    return Query.createNew("test");
  }
}

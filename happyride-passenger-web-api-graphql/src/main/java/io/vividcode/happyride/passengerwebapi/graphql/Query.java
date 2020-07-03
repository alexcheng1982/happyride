package io.vividcode.happyride.passengerwebapi.graphql;

import graphql.kickstart.tools.GraphQLQueryResolver;
import io.vividcode.happyride.addressservice.api.AddressVO;
import java.math.BigDecimal;
import java.util.Collections;

public class Query implements GraphQLQueryResolver {

  public Passenger passenger(final String id) {
    final Passenger result = new Passenger();
    result.setId(id);
    result.setName("test");
    result.setEmail("test@test.com");
    result.setMobilePhoneNumber("13434");
    result.setUserAddresses(Collections.emptyList());
    return result;
  }

  public AddressVO address(final String id) {
    return Query.createNew(id);
  }

  public static AddressVO createNew(final String id) {
    final AddressVO result = new AddressVO();
    result.setId(id);
    result.setAreaId(1);
    result.setAddressLine("test");
    result.setLat(BigDecimal.ZERO);
    result.setLng(BigDecimal.ZERO);
    result.setAreas(Collections.emptyList());
    return result;
  }
}

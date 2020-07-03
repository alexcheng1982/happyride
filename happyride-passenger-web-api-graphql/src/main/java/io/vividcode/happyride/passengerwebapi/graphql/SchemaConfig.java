package io.vividcode.happyride.passengerwebapi.graphql;

import graphql.kickstart.tools.SchemaParser;
import graphql.schema.GraphQLSchema;
import io.vividcode.happyride.addressservice.api.AddressVO;
import io.vividcode.happyride.addressservice.api.AreaVO;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SchemaConfig {

  @Bean
  public GraphQLSchema graphQLSchema() {
    return SchemaParser.newParser().file("passenger-api.graphqls")
        .dictionary("Passenger", Passenger.class)
        .dictionary("UserAddress", UserAddress.class)
        .dictionary("Address", AddressVO.class)
        .dictionary("Area", AreaVO.class)
        .resolvers(new Query())
        .build()
        .makeExecutableSchema();
  }
}

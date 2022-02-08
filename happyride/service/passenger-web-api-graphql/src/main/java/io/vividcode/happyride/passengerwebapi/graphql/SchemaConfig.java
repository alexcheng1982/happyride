package io.vividcode.happyride.passengerwebapi.graphql;

import graphql.kickstart.tools.SchemaParser;
import graphql.schema.GraphQLSchema;
import org.dataloader.DataLoader;
import org.dataloader.DataLoaderRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SchemaConfig {

  public static final String USER_ADDRESS_DATA_LOADER = "userAddress";

  @Autowired
  Query query;

  @Autowired
  Mutation mutation;

  @Autowired
  UserAddressLoader userAddressLoader;

  @Bean
  public GraphQLSchema graphQLSchema() {
    return SchemaParser.newParser()
        .file("passenger-api.graphqls")
        .resolvers(this.query, this.mutation)
        .build()
        .makeExecutableSchema();
  }

  @Bean
  public DataLoaderRegistry dataLoaderRegistry() {
    final DataLoaderRegistry dataLoaderRegistry = new DataLoaderRegistry();
    dataLoaderRegistry
        .register(USER_ADDRESS_DATA_LOADER,
            new DataLoader<>(this.userAddressLoader));
    return dataLoaderRegistry;
  }
}

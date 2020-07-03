package io.vividcode.happyride.passengerwebapi.graphql;

import graphql.kickstart.tools.SchemaParser;
import graphql.schema.GraphQLSchema;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SchemaConfig {

  @Bean
  public GraphQLSchema graphQLSchema() {
    return SchemaParser.newParser()
        .file("passenger-api.graphqls")
        .resolvers(new Query())
        .build()
        .makeExecutableSchema();
  }
}

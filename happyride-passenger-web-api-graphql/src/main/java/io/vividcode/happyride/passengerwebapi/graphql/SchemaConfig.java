package io.vividcode.happyride.passengerwebapi.graphql;

import graphql.kickstart.tools.SchemaParser;
import graphql.schema.GraphQLSchema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SchemaConfig {

  @Autowired
  Query query;

  @Autowired
  Mutation mutation;

  @Bean
  public GraphQLSchema graphQLSchema() {
    return SchemaParser.newParser()
        .file("passenger-api.graphqls")
        .resolvers(this.query, this.mutation)
        .build()
        .makeExecutableSchema();
  }
}

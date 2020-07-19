package io.vividcode.happyride.passengerwebapi.graphql;

import graphql.kickstart.execution.context.DefaultGraphQLContext;
import graphql.kickstart.execution.context.GraphQLContext;
import graphql.kickstart.servlet.context.DefaultGraphQLServletContext;
import graphql.kickstart.servlet.context.DefaultGraphQLWebSocketContext;
import graphql.kickstart.servlet.context.GraphQLServletContextBuilder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.websocket.Session;
import javax.websocket.server.HandshakeRequest;
import org.dataloader.DataLoaderRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CustomGraphQLContextBuilder implements
    GraphQLServletContextBuilder {

  @Autowired
  DataLoaderRegistry dataLoaderRegistry;

  @Override
  public GraphQLContext build() {
    return new DefaultGraphQLContext(this.dataLoaderRegistry, null);
  }

  @Override
  public GraphQLContext build(HttpServletRequest httpServletRequest,
      HttpServletResponse httpServletResponse) {
    return DefaultGraphQLServletContext
        .createServletContext(this.dataLoaderRegistry, null)
        .with(httpServletRequest)
        .with(httpServletResponse)
        .build();
  }

  @Override
  public GraphQLContext build(Session session,
      HandshakeRequest handshakeRequest) {
    return DefaultGraphQLWebSocketContext
        .createWebSocketContext(this.dataLoaderRegistry, null)
        .with(session)
        .with(handshakeRequest)
        .build();
  }
}

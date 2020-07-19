package io.vividcode.happyride.passengerwebapi.graphql;

import static io.vividcode.happyride.passengerwebapi.graphql.SchemaConfig.USER_ADDRESS_DATA_LOADER;

import graphql.kickstart.servlet.context.GraphQLServletContext;
import graphql.schema.DataFetchingEnvironment;
import io.opentracing.Scope;
import io.opentracing.Span;
import io.opentracing.Tracer;
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
    GraphQLServletContext context = environment.getContext();
    Tracer tracer = (Tracer) context.getHttpServletRequest().getServletContext()
        .getAttribute(Tracer.class.getName());
    Span span = tracer.buildSpan("getAddress")
        .withTag("userAddressId", this.id)
        .withTag("addressId", this.addressId)
        .start();
    try (Scope ignored = tracer.activateSpan(span)) {
      return context.getDataLoaderRegistry()
          .map(
              registry -> registry.
                  <String, AddressVO>getDataLoader(USER_ADDRESS_DATA_LOADER)
                  .load(this.addressId))
          .orElse(CompletableFuture
              .completedFuture(AddressVO.nullObject(this.addressId)))
          .whenComplete((addressVO, throwable) -> span.finish());
    }
  }
}

package io.vividcode.happyride.addressservice;

import static org.assertj.core.api.Assertions.assertThat;

import io.grpc.Channel;
import io.grpc.ManagedChannelBuilder;
import io.vividcode.happyride.addressservice.grpc.Address;
import io.vividcode.happyride.addressservice.grpc.AddressSearchRequest;
import io.vividcode.happyride.addressservice.grpc.AddressServiceGrpc;
import io.vividcode.happyride.addressservice.grpc.AddressServiceGrpc.AddressServiceBlockingStub;
import java.util.Iterator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Address gRPC Service")
public class AddressGrpcServiceTest {

  @Test
  @DisplayName("Search address")
  public void testSearchAddress() {
    Channel channel = ManagedChannelBuilder.forAddress("localhost", 6565)
        .usePlaintext().build();
    AddressServiceBlockingStub blockingStub = AddressServiceGrpc
        .newBlockingStub(channel);
    Iterator<Address> result = blockingStub
        .search(AddressSearchRequest.newBuilder()
            .setAreaCode(110101001015L)
            .setQuery("王府井社区居委会")
            .build());
    assertThat(result).hasNext();
  }
}

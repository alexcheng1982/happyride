package io.vividcode.happyride.addressservice;

import io.grpc.Channel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import io.vividcode.happyride.addressservice.grpc.Address;
import io.vividcode.happyride.addressservice.grpc.AddressSearchRequest;
import io.vividcode.happyride.addressservice.grpc.AddressServiceGrpc;
import io.vividcode.happyride.addressservice.grpc.AddressServiceGrpc.AddressServiceBlockingStub;
import io.vividcode.happyride.addressservice.grpc.AddressServiceGrpc.AddressServiceStub;
import io.vividcode.happyride.addressservice.grpc.GetAddressRequest;
import java.util.Iterator;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class AddressGrpcServiceTest {

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
    result.forEachRemaining(System.out::println);
  }

  public void testGetAddresses() throws InterruptedException {
    Channel channel = ManagedChannelBuilder.forAddress("localhost", 6565)
        .usePlaintext().build();
    AddressServiceStub asyncStub = AddressServiceGrpc.newStub(channel);
    CountDownLatch finishLatch = new CountDownLatch(1);
    StreamObserver<GetAddressRequest> requestObserver = asyncStub
        .getAddresses(new StreamObserver<Address>() {
          @Override
          public void onNext(Address value) {
            System.out.println(value);
          }

          @Override
          public void onError(Throwable t) {
            t.printStackTrace();
            finishLatch.countDown();
          }

          @Override
          public void onCompleted() {
            System.out.println("Completed");
            finishLatch.countDown();
          }
        });
    for (int i = 1; i <= 3; i++) {
      requestObserver.onNext(
          GetAddressRequest.newBuilder()
              .setAddressId("962fddbc-54cc-4758-bf01-56e2833c6443")
              .setAreaLevel(i).build());
    }

    requestObserver.onCompleted();
    finishLatch.await(1, TimeUnit.MINUTES);
  }

  public static void main(String[] args) throws InterruptedException {
    AddressGrpcServiceTest test = new AddressGrpcServiceTest();
    test.testSearchAddress();
    test.testGetAddresses();
  }
}

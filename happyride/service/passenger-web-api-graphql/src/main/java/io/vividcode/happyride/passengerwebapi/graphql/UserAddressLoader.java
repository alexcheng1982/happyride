package io.vividcode.happyride.passengerwebapi.graphql;

import io.opentracing.Scope;
import io.opentracing.Span;
import io.opentracing.Tracer;
import io.vividcode.happyride.addressservice.api.AddressVO;
import io.vividcode.happyride.addressservice.api.web.AddressBatchRequest;
import io.vividcode.happyride.addressservice.client.ApiCallback;
import io.vividcode.happyride.addressservice.client.ApiException;
import io.vividcode.happyride.addressservice.client.api.AddressApi;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import lombok.SneakyThrows;
import org.dataloader.BatchLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
class UserAddressLoader implements BatchLoader<String, AddressVO> {

  @Autowired
  AddressApi addressApi;

  @Autowired
  Tracer tracer;

  @SneakyThrows(ApiException.class)
  @Override
  public CompletionStage<List<AddressVO>> load(List<String> keys) {
    Span span = this.tracer.buildSpan("loadAddress")
        .withTag("addressIds", keys.toString()).start();
    try (Scope ignored = this.tracer.activateSpan(span)) {
      CompletableFuture<List<AddressVO>> future = new CompletableFuture<>();
      this.addressApi.getAddressesAsync(
          new AddressBatchRequest(keys),
          new ApiCallback<List<AddressVO>>() {
            @Override
            public void onFailure(ApiException e, int statusCode,
                Map<String, List<String>> responseHeaders) {
              future.completeExceptionally(e);
            }

            @Override
            public void onSuccess(List<AddressVO> result,
                int statusCode,
                Map<String, List<String>> responseHeaders) {
              future.complete(result);
            }

            @Override
            public void onUploadProgress(long bytesWritten,
                long contentLength, boolean done) {

            }

            @Override
            public void onDownloadProgress(long bytesRead,
                long contentLength, boolean done) {

            }
          });
      return future.whenComplete((result, throwable) -> span.finish());
    }
  }
}

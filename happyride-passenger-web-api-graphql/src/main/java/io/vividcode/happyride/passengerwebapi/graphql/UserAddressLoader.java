package io.vividcode.happyride.passengerwebapi.graphql;

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

  @SneakyThrows(ApiException.class)
  @Override
  public CompletionStage<List<AddressVO>> load(final List<String> keys) {
    final CompletableFuture<List<AddressVO>> future = new CompletableFuture<>();
    this.addressApi.getAddressesAsync(
        new AddressBatchRequest(keys),
        new ApiCallback<List<AddressVO>>() {
          @Override
          public void onFailure(final ApiException e, final int statusCode,
              final Map<String, List<String>> responseHeaders) {
            future.completeExceptionally(e);
          }

          @Override
          public void onSuccess(final List<AddressVO> result,
              final int statusCode,
              final Map<String, List<String>> responseHeaders) {
            future.complete(result);
          }

          @Override
          public void onUploadProgress(final long bytesWritten,
              final long contentLength, final boolean done) {

          }

          @Override
          public void onDownloadProgress(final long bytesRead,
              final long contentLength, final boolean done) {

          }
        });
    return future;
  }
}

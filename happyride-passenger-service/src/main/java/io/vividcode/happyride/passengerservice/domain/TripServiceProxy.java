package io.vividcode.happyride.passengerservice.domain;

import io.vividcode.happyride.tripservice.api.web.CreateTripRequest;
import io.vividcode.happyride.tripservice.client.ApiException;
import io.vividcode.happyride.tripservice.client.api.TripApi;
import org.springframework.stereotype.Component;

@Component
public class TripServiceProxy {

  private TripApi tripApi = new TripApi();

  public void createTrip(CreateTripRequest request) throws ApiException {
    tripApi.createTrip(request);
  }
}

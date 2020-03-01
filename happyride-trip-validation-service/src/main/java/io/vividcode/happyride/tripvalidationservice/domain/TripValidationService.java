package io.vividcode.happyride.tripvalidationservice.domain;

import com.google.common.collect.Lists;
import io.vividcode.happyride.tripservice.api.events.TripDetails;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class TripValidationService {

  private List<String> passengerBlackList = Lists.newArrayList("bad_passenger1", "bad_passenger2");

  public void validateTrip(TripDetails tripDetails) {
    if (passengerBlackList.contains(tripDetails.getPassengerId())) {
      throw new TripValidationException();
    }
  }
}

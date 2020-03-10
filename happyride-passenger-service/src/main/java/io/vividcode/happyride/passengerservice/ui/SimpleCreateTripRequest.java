package io.vividcode.happyride.passengerservice.ui;

import io.vividcode.happyride.common.PositionView;
import io.vividcode.happyride.tripservice.api.web.CreateTripRequest;
import java.math.BigDecimal;
import lombok.Data;

@Data
public class SimpleCreateTripRequest {

  private BigDecimal startLng;

  private BigDecimal startLat;

  private BigDecimal endLng;

  private BigDecimal endLat;

  public CreateTripRequest toCreateTripRequest(String passengerId) {
    return new CreateTripRequest(passengerId,
        new PositionView(startLng, startLat),
        new PositionView(endLng, endLat));
  }
}

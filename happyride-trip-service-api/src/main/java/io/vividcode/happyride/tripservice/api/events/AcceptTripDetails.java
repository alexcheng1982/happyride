package io.vividcode.happyride.tripservice.api.events;

import java.math.BigDecimal;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@NoArgsConstructor
public class AcceptTripDetails {
  @NonNull
  private String tripId;

  @NonNull
  private String driverId;

  @NonNull
  private BigDecimal lng;

  @NonNull
  private BigDecimal lat;
}

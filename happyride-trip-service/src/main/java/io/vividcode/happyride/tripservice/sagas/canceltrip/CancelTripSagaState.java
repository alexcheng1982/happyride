package io.vividcode.happyride.tripservice.sagas.canceltrip;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@NoArgsConstructor
@RequiredArgsConstructor
public class CancelTripSagaState {
  @NonNull
  private String tripId;
}

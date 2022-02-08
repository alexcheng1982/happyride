package io.vividcode.happyride.passengerservice.api.events;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@NoArgsConstructor
@RequiredArgsConstructor
public class PassengerDetails {

  @NonNull
  private String passengerName;
}

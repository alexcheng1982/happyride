package io.vividcode.happyride.dispatcherservice.service;

import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class AvailableDriver {
  @NonNull
  private String driverId;

  @NonNull
  private double distance;
}

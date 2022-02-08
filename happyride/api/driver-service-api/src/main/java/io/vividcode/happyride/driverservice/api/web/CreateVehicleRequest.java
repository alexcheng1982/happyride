package io.vividcode.happyride.driverservice.api.web;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@NoArgsConstructor
public class CreateVehicleRequest {
  @NonNull
  private String make;

  @NonNull
  private String mode;

  @NonNull
  private Integer year;

  @NonNull
  private String registration;
}

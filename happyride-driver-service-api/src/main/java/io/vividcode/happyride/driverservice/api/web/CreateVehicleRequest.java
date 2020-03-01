package io.vividcode.happyride.driverservice.api.web;

import lombok.Data;

@Data
public class CreateVehicleRequest {

  private String mode;

  private String make;

  private Integer year;

  private String registration;
}

package io.vividcode.happyride.driverservice.api.web;

import lombok.Data;

@Data
public class VehicleView {
  private String id;

  private String make;

  private String mode;

  private Integer year;

  private String registration;
}

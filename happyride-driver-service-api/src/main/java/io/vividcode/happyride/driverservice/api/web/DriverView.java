package io.vividcode.happyride.driverservice.api.web;

import java.util.ArrayList;
import java.util.List;
import lombok.Data;

@Data
public class DriverView {
  private String id;

  private String name;

  private String email;

  private String mobilePhoneNumber;

  private String state;

  private List<VehicleView> vehicles = new ArrayList<>();
}

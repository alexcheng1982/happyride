package io.vividcode.happyride.driverservice.api.web;

import java.util.Set;
import lombok.Data;

@Data
public class CreateDriverRequest {
  private String name;

  private String email;

  private String mobilePhoneNumber;

  private Set<CreateVehicleRequest> vehicles;
}

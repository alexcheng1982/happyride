package io.vividcode.happyride.driverservice.api.web;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@NoArgsConstructor
public class CreateDriverRequest {

  @NonNull
  private String name;

  private String email;

  @NonNull
  private String mobilePhoneNumber;

  private List<CreateVehicleRequest> vehicles = new ArrayList<>();
}

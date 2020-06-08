package io.vividcode.happyride.passengerservice.web;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@NoArgsConstructor
@RequiredArgsConstructor
public class LoginRequest {
  @NonNull
  private String username;
  @NonNull
  private String password;
}

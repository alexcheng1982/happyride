package io.vividcode.happyride.passengerservice.web;

import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;

@Service
public class ProtectedService {

  @Secured("ROLE_ADMIN")
  public void doSomething() {
    System.out.println("Do this");
  }
}

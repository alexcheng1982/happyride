package io.vividcode.happyride.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/protected")
public class ProtectedController {

  @Autowired
  ProtectedService protectedService;

  @GetMapping
  public void doSomething() {
    this.protectedService.doSomething();
  }
}

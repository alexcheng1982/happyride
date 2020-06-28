package io.vividcode.happyride.security;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

  @GetMapping
  public UserInfo getCurrentUser() {
    return new UserInfo(
        (String) SecurityContextHolder.getContext().getAuthentication()
            .getPrincipal());
  }
}

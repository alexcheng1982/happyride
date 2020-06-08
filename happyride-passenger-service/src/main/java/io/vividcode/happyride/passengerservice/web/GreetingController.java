package io.vividcode.happyride.passengerservice.web;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class GreetingController {

  @GetMapping("/hello")
  public String index(final Model model) {
    model.addAttribute("username", SecurityContextHolder.getContext().getAuthentication().getPrincipal());
    return "hello";
  }
}

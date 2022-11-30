package io.vividcode.happyride.addressservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportRuntimeHints;

@ImportRuntimeHints(DefaultRuntimeHintsRegistrar.class)
@SpringBootApplication
public class AddressServiceApplication {

  public static void main(String[] args) {
    SpringApplication.run(AddressServiceApplication.class, args);
  }
}

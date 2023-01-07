package io.vividcode.happyride.addressservice;

import io.vividcode.happyride.common.DatabaseRuntimeHintsRegistrar;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportRuntimeHints;

@ImportRuntimeHints(DatabaseRuntimeHintsRegistrar.class)
@SpringBootApplication
public class AddressServiceApplication {

  public static void main(String[] args) {
    SpringApplication.run(AddressServiceApplication.class, args);
  }
}

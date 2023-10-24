package io.vividcode.happyride.driverservice;

import io.vividcode.happyride.common.DatabaseRuntimeHintsRegistrar;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportRuntimeHints;

@ImportRuntimeHints(DatabaseRuntimeHintsRegistrar.class)
@SpringBootApplication
public class DriverServiceApplication {

  public static void main(String[] args) {
    SpringApplication.run(DriverServiceApplication.class, args);
  }
}

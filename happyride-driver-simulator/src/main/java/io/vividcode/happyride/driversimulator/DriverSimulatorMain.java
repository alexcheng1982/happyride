package io.vividcode.happyride.driversimulator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class DriverSimulatorMain {

  public static void main(String[] args) {
    SpringApplication.run(DriverSimulatorMain.class, args);
  }
}

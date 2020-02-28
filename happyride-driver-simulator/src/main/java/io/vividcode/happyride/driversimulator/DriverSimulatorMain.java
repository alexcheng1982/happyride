package io.vividcode.happyride.driversimulator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DriverSimulatorMain implements CommandLineRunner {
  @Autowired
  DriverSimulatorFactory simulatorFactory;

  public static void main(String[] args) {
    SpringApplication.run(DriverSimulatorMain.class, args);
  }

  @Override
  public void run(String... args) throws Exception {
    for (int i = 0; i < 10; i++) {
      DriverSimulator simulator = simulatorFactory.create("driver" + i, "vehicle" + i);
      simulator.startSimulation();
    }
  }
}

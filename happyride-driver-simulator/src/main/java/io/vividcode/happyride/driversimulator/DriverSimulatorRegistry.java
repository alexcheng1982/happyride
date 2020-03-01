package io.vividcode.happyride.driversimulator;

import io.vividcode.happyride.driversimulator.web.AddDriverRequest;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DriverSimulatorRegistry {

  @Autowired
  DriverSimulatorFactory driverSimulatorFactory;

  private ConcurrentHashMap<String, DriverSimulator> simulators = new ConcurrentHashMap<>();

  public DriverSimulator add(AddDriverRequest request) {
    DriverSimulator simulator = driverSimulatorFactory
        .create(request.getDriverId(), "default_vehicle", request.getPosLng(), request.getPosLat());
    simulators.put(simulator.getId(), simulator);
    if (request.isStartSimulation()) {
      simulator.startSimulation();
    }
    return simulator;
  }

  public DriverSimulator get(String driverSimulatorId) {
    return simulators.get(driverSimulatorId);
  }

  public Collection<DriverSimulator> list() {
    return simulators.values();
  }

  public void stop(String driverSimulatorId) {
    withDriverSimulator(driverSimulatorId, DriverSimulator::stopSimulation);
  }

  public void start(String driverSimulatorId) {
    withDriverSimulator(driverSimulatorId, DriverSimulator::startSimulation);
  }

  public void resetPosition(String driverSimulatorId, BigDecimal lng, BigDecimal lat) {
    withDriverSimulator(driverSimulatorId, simulator -> simulator.resetPosition(lng, lat));
  }

  public void markAsAvailable(String driverSimulatorId) {
    withDriverSimulator(driverSimulatorId, DriverSimulator::markAsAvailable);
  }

  public void markAsNotAvailable(String driverSimulatorId) {
    withDriverSimulator(driverSimulatorId, DriverSimulator::markAsNotAvailable);
  }

  private void withDriverSimulator(String driverSimulatorId, Consumer<DriverSimulator> action) {
    DriverSimulator simulator = simulators.get(driverSimulatorId);
    if (simulator != null) {
      action.accept(simulator);
    } else {
      throw new DriverSimulatorNotFoundException(driverSimulatorId);
    }
  }
}

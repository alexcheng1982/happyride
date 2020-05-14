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

  private final ConcurrentHashMap<String, DriverSimulator> simulators = new ConcurrentHashMap<>();

  public DriverSimulator add(final AddDriverRequest request) {
    final DriverSimulator simulator = this.driverSimulatorFactory
        .create(request.getDriverId(), "default_vehicle", request.getPosLng(),
            request.getPosLat());
    this.simulators.put(simulator.getId(), simulator);
    if (request.isStartSimulation()) {
      simulator.startSimulation();
    }
    return simulator;
  }

  public DriverSimulator get(final String driverSimulatorId) {
    return this.simulators.get(driverSimulatorId);
  }

  public Collection<DriverSimulator> list() {
    return this.simulators.values();
  }

  public void stop(final String driverSimulatorId) {
    this.withDriverSimulator(driverSimulatorId, DriverSimulator::stopSimulation);
  }

  public void start(final String driverSimulatorId) {
    this.withDriverSimulator(driverSimulatorId, DriverSimulator::startSimulation);
  }

  public void resetPosition(final String driverSimulatorId, final BigDecimal lng,
      final BigDecimal lat) {
    this.withDriverSimulator(driverSimulatorId,
        simulator -> simulator.resetPosition(lng, lat));
  }

  public void markAsAvailable(final String driverSimulatorId) {
    this.withDriverSimulator(driverSimulatorId, DriverSimulator::markAsAvailable);
  }

  public void markAsNotAvailable(final String driverSimulatorId) {
    this.withDriverSimulator(driverSimulatorId, DriverSimulator::markAsNotAvailable);
  }

  private void withDriverSimulator(final String driverSimulatorId,
      final Consumer<DriverSimulator> action) {
    final DriverSimulator simulator = this.simulators.get(driverSimulatorId);
    if (simulator != null) {
      action.accept(simulator);
    } else {
      throw new DriverSimulatorNotFoundException(driverSimulatorId);
    }
  }
}

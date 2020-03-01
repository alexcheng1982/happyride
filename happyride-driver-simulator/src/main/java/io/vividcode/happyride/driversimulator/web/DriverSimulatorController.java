package io.vividcode.happyride.driversimulator.web;

import io.vividcode.happyride.driversimulator.DriverSimulator;
import io.vividcode.happyride.driversimulator.DriverSimulatorRegistry;
import io.vividcode.happyride.driversimulator.DriverSimulatorSnapshot;
import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DriverSimulatorController {

  @Autowired
  DriverSimulatorRegistry driverSimulatorRegistry;

  @GetMapping
  public List<DriverSimulatorSnapshot> list() {
    return driverSimulatorRegistry.list().stream().map(DriverSimulator::dump)
        .collect(Collectors.toList());
  }

  @GetMapping("{id}")
  public ResponseEntity<DriverSimulatorSnapshot> getDriverSimulator(
      @PathVariable("id") String driverSimulatorId) {
    return Optional.ofNullable(driverSimulatorRegistry.get(driverSimulatorId))
        .map(simulator -> ResponseEntity.ok(simulator.dump()))
        .orElseGet(() -> ResponseEntity.notFound().build());
  }

  @PostMapping
  public ResponseEntity<Void> addDriver(@RequestBody AddDriverRequest request) {
    DriverSimulator simulator = driverSimulatorRegistry.add(request);
    return ResponseEntity.created(URI.create("/" + simulator.getId())).build();
  }

  @PostMapping("{id}/start")
  public void startSimulation(@PathVariable("id") String driverSimulatorId) {
    driverSimulatorRegistry.start(driverSimulatorId);
  }

  @PostMapping("{id}/stop")
  public void stopSimulation(@PathVariable("id") String driverSimulatorId) {
    driverSimulatorRegistry.stop(driverSimulatorId);
  }

  @PostMapping("{id}/resetPosition")
  public void resetPosition(@PathVariable("id") String driverSimulatorId,
      @RequestBody ResetPositionRequest request) {
    driverSimulatorRegistry
        .resetPosition(driverSimulatorId, request.getPosLng(), request.getPosLat());
  }

  @PostMapping("{id}/markAsAvailable")
  public void markAsAvailable(@PathVariable("id") String driverSimulatorId) {
    driverSimulatorRegistry.markAsAvailable(driverSimulatorId);
  }

  @PostMapping("{id}/markAsNotAvailable")
  public void markAsNotAvailable(@PathVariable("id") String driverSimulatorId) {
    driverSimulatorRegistry.markAsNotAvailable(driverSimulatorId);
  }
}

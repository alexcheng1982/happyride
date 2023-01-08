package io.vividcode.happyride.driversimulator.web;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.vividcode.happyride.driversimulator.DriverSimulator;
import io.vividcode.happyride.driversimulator.DriverSimulatorRegistry;
import io.vividcode.happyride.driversimulator.DriverSimulatorSnapshot;
import java.net.URI;
import java.time.Duration;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/api")
public class DriverSimulatorController {

  @Autowired
  DriverSimulatorRegistry driverSimulatorRegistry;

  private final AtomicInteger driverId = new AtomicInteger(1);
  private final ObjectMapper objectMapper = new ObjectMapper();

  @GetMapping
  public Flux<DriverSimulatorSnapshot> list() {
    return Flux.fromIterable(this.getDrivers());
  }

  @GetMapping(value = "live", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
  public Flux<ServerSentEvent<String>> live() {
    return Flux.interval(Duration.ofSeconds(5))
        .map(sequence -> ServerSentEvent.<String>builder()
            .id(String.valueOf(sequence))
            .event("driverUpdated")
            .data(this.getDriversJsonData())
            .build());
  }

  private List<DriverSimulatorSnapshot> getDrivers() {
    return this.driverSimulatorRegistry.list().stream()
        .sorted(Comparator.comparing(DriverSimulator::getDriverId))
        .map(DriverSimulator::dump)
        .collect(Collectors.toList());
  }

  private String getDriversJsonData() {
    try {
      return this.objectMapper.writeValueAsString(this.getDrivers());
    } catch (final JsonProcessingException e) {
      return "[]";
    }
  }

  @GetMapping("{id}")
  public ResponseEntity<DriverSimulatorSnapshot> getDriverSimulator(
      @PathVariable("id") final String driverSimulatorId) {
    return Optional
        .ofNullable(this.driverSimulatorRegistry.get(driverSimulatorId))
        .map(simulator -> ResponseEntity.ok(simulator.dump()))
        .orElseGet(() -> ResponseEntity.notFound().build());
  }

  @PostMapping("/")
  public ResponseEntity<Void> addDriver(
      @RequestBody final AddDriverRequest request,
      final ServerHttpRequest httpRequest) {
    final DriverSimulator simulator = this.driverSimulatorRegistry.add(request);
    return ResponseEntity
        .created(this.resourceCreated(httpRequest, simulator.getId()))
        .build();
  }

  @PostMapping("quickAdd")
  public ResponseEntity<Void> quickAddDriver(
      final ServerHttpRequest httpRequest) {
    return this.addDriver(new AddDriverRequest(
            String.format("Driver-%02d", this.driverId.getAndIncrement())),
        httpRequest);
  }

  @PostMapping("{id}/start")
  public void startSimulation(
      @PathVariable("id") final String driverSimulatorId) {
    this.driverSimulatorRegistry.start(driverSimulatorId);
  }

  @PostMapping("{id}/stop")
  public void stopSimulation(
      @PathVariable("id") final String driverSimulatorId) {
    this.driverSimulatorRegistry.stop(driverSimulatorId);
  }

  @PostMapping("{id}/resetPosition")
  public void resetPosition(@PathVariable("id") final String driverSimulatorId,
      @RequestBody final ResetPositionRequest request) {
    this.driverSimulatorRegistry
        .resetPosition(driverSimulatorId, request.getPosLng(),
            request.getPosLat());
  }

  @PostMapping("{id}/markAsAvailable")
  public void markAsAvailable(
      @PathVariable("id") final String driverSimulatorId) {
    this.driverSimulatorRegistry.markAsAvailable(driverSimulatorId);
  }

  @PostMapping("{id}/markAsNotAvailable")
  public void markAsNotAvailable(
      @PathVariable("id") final String driverSimulatorId) {
    this.driverSimulatorRegistry.markAsNotAvailable(driverSimulatorId);
  }

  private URI resourceCreated(final HttpRequest request,
      final String resourceId) {
    return UriComponentsBuilder.fromHttpRequest(request)
        .replacePath("/{id}")
        .buildAndExpand(resourceId)
        .toUri();
  }
}

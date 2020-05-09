package io.vividcode.happyride.driversimulator;

import io.vividcode.happyride.common.DriverState;
import io.vividcode.happyride.dispatchservice.api.events.DriverLocation;
import io.vividcode.happyride.dispatchservice.api.events.DriverLocationUpdatedEvent;
import java.math.BigDecimal;
import java.time.Duration;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import org.axonframework.eventhandling.gateway.EventGateway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.EmitterProcessor;
import reactor.core.publisher.Flux;

public class DriverSimulator {

  private final String id;
  private final EventGateway eventGateway;

  private final int[][] deltas = new int[][]{
      new int[]{1, 0},
      new int[]{0, -1},
      new int[]{-1, 0},
      new int[]{0, 1}
  };

  private int direction = 0;
  private DriverLocation currentLocation;
  private DriverState state = DriverState.OFFLINE;
  private EmitterProcessor<Boolean> stop;
  private final String driverId;
  private final String vehicleId;

  private static final Logger LOGGER = LoggerFactory
      .getLogger(DriverSimulator.class);

  public DriverSimulator(final String driverId, final String vehicleId,
      final EventGateway eventGateway,
      final DriverLocation initialLocation) {
    this.id = UUID.randomUUID().toString();
    this.driverId = driverId;
    this.vehicleId = vehicleId;
    this.currentLocation = initialLocation;
    this.eventGateway = eventGateway;
  }

  public String getId() {
    return this.id;
  }

  public String getDriverId() {
    return this.driverId;
  }

  public void startSimulation() {
    LOGGER.info("Start simulation for driver [{}] with vehicle [{}]",
        this.driverId,
        this.vehicleId);
    this.state = DriverState.AVAILABLE;
    this.stop = EmitterProcessor.create();
    Flux.interval(Duration.ofSeconds(5))
        .takeUntilOther(this.stop)
        .subscribe((v) -> {
          this.updateLocation();
          this.sendLocation();
        });
  }

  public void stopSimulation() {
    LOGGER.info("Stop simulation for driver [{}] with vehicle [{}]",
        this.driverId,
        this.vehicleId);
    this.state = DriverState.OFFLINE;
    this.stop.onNext(true);
  }

  public void resetPosition(final BigDecimal lng, final BigDecimal lat) {
    this.currentLocation = this.currentLocation.resetTo(lng, lat);
  }

  public void markAsAvailable() {
    this.state = DriverState.AVAILABLE;
  }

  public void markAsNotAvailable() {
    this.state = DriverState.NOT_AVAILABLE;
  }

  private void sendLocation() {
    final DriverLocationUpdatedEvent event = new DriverLocationUpdatedEvent();
    event.setTimestamp(System.currentTimeMillis());
    event.setLocation(this.currentLocation);
    event.setState(this.state);
    this.eventGateway.publish(event);
  }

  private void updateLocation() {
    final ThreadLocalRandom random = ThreadLocalRandom.current();
    final boolean shouldTurn = random.nextInt(3) > 1;
    if (shouldTurn) {
      final boolean turnLeft = random.nextBoolean();
      if (turnLeft) {
        this.direction = (this.direction + 3) % 4;
      } else {
        this.direction = this.direction++ % 4;
      }
    }
    final int speed = random.nextInt(1, 4);
    final double latDelta = this.deltas[this.direction][0] * 0.000001 * speed;
    final double lngDelta = this.deltas[this.direction][1] * 0.000001 * speed;
    this.currentLocation = this.currentLocation
        .moveTo(BigDecimal.valueOf(lngDelta), BigDecimal.valueOf(latDelta));
  }

  public DriverSimulatorSnapshot dump() {
    final DriverSimulatorSnapshot snapshot = new DriverSimulatorSnapshot();
    snapshot.setId(this.id);
    snapshot.setDriverId(this.driverId);
    snapshot.setVehicleId(this.vehicleId);
    snapshot.setState(this.state);
    snapshot.setPosLng(this.currentLocation.getLng());
    snapshot.setPosLat(this.currentLocation.getLat());
    return snapshot;
  }
}

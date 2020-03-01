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

  private static final Logger LOGGER = LoggerFactory.getLogger(DriverSimulator.class);

  public DriverSimulator(String driverId, String vehicleId, EventGateway eventGateway,
      DriverLocation initialLocation) {
    this.id = UUID.randomUUID().toString();
    this.driverId = driverId;
    this.vehicleId = vehicleId;
    currentLocation = initialLocation;
    this.eventGateway = eventGateway;
  }

  public String getId() {
    return id;
  }

  public void startSimulation() {
    LOGGER.info("Start simulation for driver [{}] with vehicle [{}]", driverId, vehicleId);
    state = DriverState.AVAILABLE;
    stop = EmitterProcessor.create();
    Flux.interval(Duration.ofSeconds(5))
        .takeUntilOther(stop)
        .subscribe((v) -> {
          updateLocation();
          sendLocation();
        });
  }

  public void stopSimulation() {
    LOGGER.info("Stop simulation for driver [{}] with vehicle [{}]", driverId, vehicleId);
    state = DriverState.OFFLINE;
    stop.onNext(true);
  }

  public void resetPosition(BigDecimal lng, BigDecimal lat) {
    currentLocation = currentLocation.resetTo(lng, lat);
  }

  public void markAsAvailable() {
    state = DriverState.AVAILABLE;
  }

  public void markAsNotAvailable() {
    state = DriverState.NOT_AVAILABLE;
  }

  private void sendLocation() {
    DriverLocationUpdatedEvent event = new DriverLocationUpdatedEvent();
    event.setTimestamp(System.currentTimeMillis());
    event.setLocation(currentLocation);
    event.setState(state);
    eventGateway.publish(event);
  }

  private void updateLocation() {
    ThreadLocalRandom random = ThreadLocalRandom.current();
    boolean shouldTurn = random.nextInt(3) > 1;
    if (shouldTurn) {
      boolean turnLeft = random.nextBoolean();
      if (turnLeft) {
        direction = (direction + 3) % 4;
      } else {
        direction = direction++ % 4;
      }
    }
    int speed = random.nextInt(1, 4);
    double latDelta = deltas[direction][0] * 0.000001 * speed;
    double lngDelta = deltas[direction][1] * 0.000001 * speed;
    currentLocation = currentLocation
        .moveTo(BigDecimal.valueOf(lngDelta), BigDecimal.valueOf(latDelta));
  }

  public DriverSimulatorSnapshot dump() {
    DriverSimulatorSnapshot snapshot = new DriverSimulatorSnapshot();
    snapshot.setId(id);
    snapshot.setDriverId(driverId);
    snapshot.setVehicleId(vehicleId);
    snapshot.setState(state);
    snapshot.setPosLng(currentLocation.getLng());
    snapshot.setPosLat(currentLocation.getLat());
    return snapshot;
  }
}

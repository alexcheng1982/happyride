package io.vividcode.happyride.dispatchservice;

import io.vividcode.happyride.dispatchservice.api.events.TripDispatchFailedReason;
import io.vividcode.happyride.tripservice.api.events.DriverAcceptTripDetails;
import io.vividcode.happyride.tripservice.api.events.TripDetails;
import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ScheduledFuture;
import java.util.function.BiConsumer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.GeoResults;
import org.springframework.data.geo.Point;
import org.springframework.data.redis.connection.RedisGeoCommands.DistanceUnit;
import org.springframework.data.redis.connection.RedisGeoCommands.GeoLocation;
import org.springframework.data.redis.connection.RedisGeoCommands.GeoRadiusCommandArgs;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class TripAcceptanceService {

  @Autowired
  RedisTemplate<String, String> redisTemplate;

  @Autowired
  TaskScheduler taskScheduler;

  private final Distance searchRadius = new Distance(10,
      DistanceUnit.KILOMETERS);
  private final String passenger = "__passenger__";
  private final int acceptanceCheckMaxTimes = 3;
  private final Map<String, ScheduledFuture<?>> tasks = new HashMap<>();

  public void startTripAcceptanceCheck(final String tripId,
      final TripDetails tripDetails,
      final Duration interval,
      final BiConsumer<String, String> successCallback,
      final BiConsumer<String, TripDispatchFailedReason> failureCallback) {
    this.redisTemplate.opsForGeo()
        .add(this.keyForTripAcceptance(tripId),
            new Point(tripDetails.getStartPos().getLng().doubleValue(),
                tripDetails.getStartPos().getLat().doubleValue()),
            this.passenger);
    this.scheduleCheckTripAcceptanceTask(tripId, interval, successCallback,
        failureCallback, 1);
  }

  private void scheduleCheckTripAcceptanceTask(
      final String tripId,
      final Duration interval,
      final BiConsumer<String, String> successCallback,
      final BiConsumer<String, TripDispatchFailedReason> failureCallback,
      final int attempt) {
    this.tasks.put(tripId,
        this.taskScheduler
            .schedule(
                new CheckTripAcceptanceTask(tripId, interval, successCallback,
                    failureCallback,
                    attempt),
                Instant.now().plusMillis(interval.toMillis())));
  }

  public void cancelTripAcceptanceCheck(final String tripId) {
    Optional.ofNullable(this.tasks.get(tripId))
        .ifPresent(future -> future.cancel(true));
  }

  public void addDriverToAcceptTrip(final String tripId,
      final DriverAcceptTripDetails details) {
    this.redisTemplate.opsForGeo()
        .add(this.keyForTripAcceptance(tripId),
            new Point(details.getPosLng().doubleValue(),
                details.getPosLat().doubleValue()), details.getDriverId());
  }

  private Optional<String> findDriverToAcceptTrip(final String tripId) {
    final GeoResults<GeoLocation<String>> results = this.redisTemplate
        .opsForGeo()
        .radius(this.keyForTripAcceptance(tripId), this.passenger,
            this.searchRadius,
            GeoRadiusCommandArgs.newGeoRadiusArgs().sortAscending());
    return results.getContent().stream()
        .map(result -> result.getContent().getName())
        .filter(name -> !Objects.equals(name, this.passenger))
        .findFirst();
  }

  private String keyForTripAcceptance(final String tripId) {
    return String.format("accept_trip_%s", tripId);
  }

  private class CheckTripAcceptanceTask implements Runnable {

    private final String tripId;
    private final Duration interval;
    private final int attempt;
    private final BiConsumer<String, String> successCallback;
    private final BiConsumer<String, TripDispatchFailedReason> failureCallback;

    CheckTripAcceptanceTask(final String tripId, final Duration interval,
        final BiConsumer<String, String> successCallback,
        final BiConsumer<String, TripDispatchFailedReason> failureCallback,
        final int attempt) {
      this.tripId = tripId;
      this.interval = interval;
      this.successCallback = successCallback;
      this.failureCallback = failureCallback;
      this.attempt = attempt;
    }

    @Override
    public void run() {
      if (this.attempt > TripAcceptanceService.this.acceptanceCheckMaxTimes) {
        TripAcceptanceService.log
            .warn("No acceptance for trip {}, notify dispatch failed",
                this.tripId);
        this.failureCallback
            .accept(this.tripId, TripDispatchFailedReason.NO_DRIVERS_ACCEPTED);
        return;
      }
      TripAcceptanceService.log
          .info("Check acceptance for trip {}", this.tripId);
      final Optional<String> driverToAcceptTrip = TripAcceptanceService.this
          .findDriverToAcceptTrip(
              this.tripId);
      if (driverToAcceptTrip.isPresent()) {
        this.successCallback.accept(this.tripId, driverToAcceptTrip.get());
      } else {
        TripAcceptanceService.log
            .info("No acceptance found for trip {}, will try again in {}",
                this.tripId, this.interval);
        TripAcceptanceService.this
            .scheduleCheckTripAcceptanceTask(this.tripId, this.interval,
                this.successCallback,
                this.failureCallback,
                this.attempt + 1);
      }
    }
  }
}

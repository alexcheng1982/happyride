package io.vividcode.happyride.dispatchservice;

import io.vividcode.happyride.dispatchservice.api.events.TripDispatchFailedReason;
import io.vividcode.happyride.tripservice.api.events.DriverAcceptTripDetails;
import io.vividcode.happyride.tripservice.api.events.TripDetails;
import java.time.Duration;
import java.time.Instant;
import java.util.Objects;
import java.util.Optional;
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

  private final Distance searchRadius = new Distance(10, DistanceUnit.KILOMETERS);
  private final String passenger = "__passenger__";
  private final int acceptanceCheckMaxTimes = 3;

  public void startTripAcceptanceCheck(String tripId, TripDetails tripDetails, Duration interval,
      BiConsumer<String, String> successCallback,
      BiConsumer<String, TripDispatchFailedReason> failureCallback) {
    redisTemplate.opsForGeo()
        .add(keyForTripAcceptance(tripId),
            new Point(tripDetails.getStartPos().getLng().doubleValue(),
                tripDetails.getStartPos().getLat().doubleValue()), passenger);
    scheduleCheckTripAcceptanceTask(tripId, interval, successCallback, failureCallback, 1);
  }

  private void scheduleCheckTripAcceptanceTask(String tripId, Duration interval,
      BiConsumer<String, String> successCallback,
      BiConsumer<String, TripDispatchFailedReason> failureCallback, int attempt) {
    taskScheduler
        .schedule(new CheckTripAcceptanceTask(tripId, interval, successCallback, failureCallback,
                attempt),
            Instant.now().plusMillis(interval.toMillis()));
  }

  public void addDriverToAcceptTrip(String tripId, DriverAcceptTripDetails details) {
    redisTemplate.opsForGeo()
        .add(keyForTripAcceptance(tripId),
            new Point(details.getPosLng().doubleValue(),
                details.getPosLat().doubleValue()), details.getDriverId());
  }

  private Optional<String> findDriverToAcceptTrip(String tripId) {
    GeoResults<GeoLocation<String>> results = redisTemplate.opsForGeo()
        .radius(keyForTripAcceptance(tripId), passenger, searchRadius,
            GeoRadiusCommandArgs.newGeoRadiusArgs().sortAscending());
    return results.getContent().stream()
        .map(result -> result.getContent().getName())
        .filter(name -> !Objects.equals(name, passenger))
        .findFirst();
  }

  private String keyForTripAcceptance(String tripId) {
    return String.format("accept_trip_%s", tripId);
  }

  private class CheckTripAcceptanceTask implements Runnable {

    private final String tripId;
    private final Duration interval;
    private final int attempt;
    private final BiConsumer<String, String> successCallback;
    private final BiConsumer<String, TripDispatchFailedReason> failureCallback;

    CheckTripAcceptanceTask(String tripId, Duration interval,
        BiConsumer<String, String> successCallback,
        BiConsumer<String, TripDispatchFailedReason> failureCallback,
        int attempt) {
      this.tripId = tripId;
      this.interval = interval;
      this.successCallback = successCallback;
      this.failureCallback = failureCallback;
      this.attempt = attempt;
    }

    @Override
    public void run() {
      if (attempt > acceptanceCheckMaxTimes) {
        log.warn("No acceptance for trip {}, notify dispatch failed", tripId);
        failureCallback.accept(tripId, TripDispatchFailedReason.NO_DRIVERS);
        return;
      }
      log.info("Check acceptance for trip {}", tripId);
      Optional<String> driverToAcceptTrip = findDriverToAcceptTrip(tripId);
      if (driverToAcceptTrip.isPresent()) {
        successCallback.accept(tripId, driverToAcceptTrip.get());
      } else {
        log.info("No acceptance found for trip {}, will try again in {}", tripId, interval);
        scheduleCheckTripAcceptanceTask(tripId, interval, successCallback, failureCallback,
            attempt + 1);
      }
    }
  }
}

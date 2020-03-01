package io.vividcode.happyride.dispatcherservice.service;

import io.eventuate.tram.events.aggregates.ResultWithDomainEvents;
import io.vividcode.happyride.common.Position;
import io.vividcode.happyride.dispatcherservice.api.events.DispatchDomainEvent;
import io.vividcode.happyride.dispatcherservice.api.events.DriverLocation;
import io.vividcode.happyride.dispatcherservice.api.events.TripDispatchFailedReason;
import io.vividcode.happyride.dispatcherservice.dataaccess.DispatchRepository;
import io.vividcode.happyride.dispatcherservice.domain.Dispatch;
import io.vividcode.happyride.dispatcherservice.domain.DispatchDomainEventPublisher;
import io.vividcode.happyride.tripservice.api.events.DriverAcceptTripDetails;
import io.vividcode.happyride.tripservice.api.events.TripDetails;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.Instant;
import java.util.Collections;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import javax.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.Circle;
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
public class DispatcherService {

  @Autowired
  RedisTemplate<String, String> redisTemplate;

  @Autowired
  TaskScheduler taskScheduler;

  @Autowired
  DispatchRepository dispatchRepository;

  @Autowired
  DispatchDomainEventPublisher eventPublisher;

  private static final Logger LOGGER = LoggerFactory.getLogger(DispatcherService.class);

  private final Distance searchRadius = new Distance(10, DistanceUnit.KILOMETERS);
  private final String key = "available_drivers";
  private final String passenger = "__passenger__";
  private final Duration acceptanceCheckInterval = Duration.ofSeconds(10);
  private final int acceptanceCheckMaxTimes = 3;


  public void addAvailableDriver(DriverLocation location) {
    redisTemplate.opsForGeo()
        .add(key, new Point(location.getLng().doubleValue(), location.getLat().doubleValue()),
            location.getDriverId());
  }

  public void removeAvailableDriver(String driverId) {
    redisTemplate.opsForGeo().remove(key, driverId);
  }

  public Set<AvailableDriver> findAvailableDrivers(double lng, double lat) {
    GeoResults<GeoLocation<String>> results = redisTemplate.opsForGeo()
        .radius(key, new Circle(new Point(lng, lat), searchRadius),
            GeoRadiusCommandArgs.newGeoRadiusArgs().includeCoordinates());
    if (results != null) {
      return results.getContent().stream().filter(Objects::nonNull)
          .map(result -> {
            GeoLocation<String> content = result.getContent();
            Point point = content.getPoint();
            return new AvailableDriver(content.getName(),
                BigDecimal.valueOf(point.getX()), BigDecimal.valueOf(point.getY()));
          })
          .collect(Collectors.toSet());
    }
    return Collections.emptySet();
  }

  public void verifyDispatch(TripDetails tripDetails) {
    Position startPos = tripDetails.getStartPos();
    Set<AvailableDriver> availableDrivers = findAvailableDrivers(
        startPos.getLng().doubleValue(),
        startPos.getLat().doubleValue()
    );
    if (availableDrivers.isEmpty()) {
      throw new DispatchVerificationException();
    }
  }

  @Transactional
  public void dispatchTrip(String tripId, TripDetails tripDetails) {
    Position startPos = tripDetails.getStartPos();
    Set<AvailableDriver> availableDrivers = findAvailableDrivers(
        startPos.getLng().doubleValue(),
        startPos.getLat().doubleValue()
    );
    saveAndPublishEvents(Dispatch.createDispatch(tripId, tripDetails, availableDrivers));
    startTripAcceptanceCheck(tripId, tripDetails, acceptanceCheckInterval);
    LOGGER.info("Dispatched trip {} to drivers {}", tripId, availableDrivers);
  }

  @Transactional
  public void submitTripAcceptance(String tripId, DriverAcceptTripDetails details) {
    LOGGER.info("Driver {} wants to accept trip {}", details.getDriverId(), tripId);
    withCurrentDispatch(tripId, dispatch -> {
      dispatchRepository.save(dispatch.submitTripAcceptance(details));
      addDriverToAcceptTrip(tripId, details);
    });
  }

  @Transactional
  public void selectTripAcceptance(String tripId, String driverId) {
    LOGGER.info("Select driver {} to accept trip {}", driverId, tripId);
    withCurrentDispatch(tripId, dispatch ->
        saveAndPublishEvents(dispatch.selectTripAcceptance(driverId)));
  }

  private void withCurrentDispatch(String tripId, Consumer<Dispatch> dispatchConsumer,
      Runnable noDispatchAction) {
    Optional<Dispatch> optionalDispatch = dispatchRepository.findCurrentDispatch(tripId);
    if (optionalDispatch.isPresent()) {
      dispatchConsumer.accept(optionalDispatch.get());
    } else {
      LOGGER.warn("No active dispatch found for trip {}", tripId);
      noDispatchAction.run();
    }
  }

  private void withCurrentDispatch(String tripId, Consumer<Dispatch> dispatchConsumer) {
    withCurrentDispatch(tripId, dispatchConsumer, () -> {
    });
  }

  private void startTripAcceptanceCheck(String tripId, TripDetails tripDetails, Duration interval) {
    redisTemplate.opsForGeo()
        .add(keyForTripAcceptance(tripId),
            new Point(tripDetails.getStartPos().getLng().doubleValue(),
                tripDetails.getStartPos().getLat().doubleValue()), passenger);
    scheduleCheckTripAcceptanceTask(tripId, interval, 1);
  }

  private void scheduleCheckTripAcceptanceTask(String tripId, Duration interval, int attempt) {
    taskScheduler
        .schedule(new CheckTripAcceptanceTask(tripId, interval, attempt),
            Instant.now().plusMillis(interval.toMillis()));
  }

  private void addDriverToAcceptTrip(String tripId, DriverAcceptTripDetails details) {
    redisTemplate.opsForGeo()
        .add(keyForTripAcceptance(tripId),
            new Point(details.getPosLng().doubleValue(),
                details.getPosLat().doubleValue()), details.getDriverId());
  }

  public Optional<String> findDriverToAcceptTrip(String tripId) {
    GeoResults<GeoLocation<String>> results = redisTemplate.opsForGeo()
        .radius(keyForTripAcceptance(tripId), passenger, searchRadius,
            GeoRadiusCommandArgs.newGeoRadiusArgs().sortAscending());
    return results.getContent().stream()
        .map(result -> result.getContent().getName())
        .filter(name -> !Objects.equals(name, passenger))
        .findFirst();
  }

  @Transactional
  public void notifyTripDispatchFailed(String tripId, TripDispatchFailedReason reason) {
    withCurrentDispatch(tripId,
        dispatch -> saveAndPublishEvents(dispatch.markAsFailed(tripId, reason)));
  }

  private void saveAndPublishEvents(ResultWithDomainEvents<Dispatch, DispatchDomainEvent> result) {
    Dispatch dispatch = result.result;
    dispatchRepository.save(dispatch);
    eventPublisher.publish(dispatch, result.events);
  }

  private String keyForTripAcceptance(String tripId) {
    return String.format("accept_trip_%s", tripId);
  }

  private class CheckTripAcceptanceTask implements Runnable {

    private final String tripId;
    private final Duration interval;
    private final int attempt;

    CheckTripAcceptanceTask(String tripId, Duration interval, int attempt) {
      this.tripId = tripId;
      this.interval = interval;
      this.attempt = attempt;
    }

    @Override
    public void run() {
      if (attempt > acceptanceCheckMaxTimes) {
        LOGGER.warn("No acceptance for trip {}, notify dispatch failed", tripId);
        notifyTripDispatchFailed(tripId, TripDispatchFailedReason.NO_DRIVERS);
        return;
      }
      LOGGER.info("Check acceptance for trip {}", tripId);
      Optional<String> driverToAcceptTrip = findDriverToAcceptTrip(tripId);
      if (driverToAcceptTrip.isPresent()) {
        selectTripAcceptance(tripId, driverToAcceptTrip.get());
      } else {
        LOGGER.info("No acceptance found for trip {}, will try again in {}", tripId, interval);
        scheduleCheckTripAcceptanceTask(tripId, interval, attempt + 1);
      }
    }
  }
}

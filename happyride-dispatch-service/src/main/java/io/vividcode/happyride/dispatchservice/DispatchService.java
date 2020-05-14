package io.vividcode.happyride.dispatchservice;

import io.eventuate.tram.events.aggregates.ResultWithDomainEvents;
import io.vividcode.happyride.common.PositionVO;
import io.vividcode.happyride.dispatchservice.api.events.DispatchDomainEvent;
import io.vividcode.happyride.dispatchservice.api.events.TripDispatchFailedReason;
import io.vividcode.happyride.dispatchservice.dataaccess.DispatchRepository;
import io.vividcode.happyride.dispatchservice.domain.Dispatch;
import io.vividcode.happyride.dispatchservice.domain.DispatchDomainEventPublisher;
import io.vividcode.happyride.tripservice.api.events.DriverAcceptTripDetails;
import io.vividcode.happyride.tripservice.api.events.TripDetails;
import java.time.Duration;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;
import javax.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class DispatchService {

  @Autowired
  DriverLocationService driverLocationService;

  @Autowired
  TripAcceptanceService tripAcceptanceService;

  @Autowired
  DispatchRepository dispatchRepository;

  @Autowired
  DispatchDomainEventPublisher eventPublisher;

  private final Duration acceptanceCheckInterval = Duration.ofSeconds(10);

  public void verifyDispatch(final TripDetails tripDetails) {
    final Set<AvailableDriver> availableDrivers = this
        .findAvailableDrivers(tripDetails);
    if (availableDrivers.isEmpty()) {
      throw new DispatchVerificationException("No available drivers");
    }
  }

  @Transactional
  public void dispatchTrip(final String tripId, final TripDetails tripDetails) {
    final Set<AvailableDriver> availableDrivers = this
        .findAvailableDrivers(tripDetails);
    this.saveAndPublishEvents(
        Dispatch.createDispatch(tripId, tripDetails, availableDrivers));
    if (!availableDrivers.isEmpty()) {
      this.tripAcceptanceService.startTripAcceptanceCheck(tripId, tripDetails,
          this.acceptanceCheckInterval,
          this::selectTripAcceptance, this::notifyTripDispatchFailed);
      log.info("Dispatch trip {} to drivers {}", tripId, availableDrivers);
    }
  }

  private Set<AvailableDriver> findAvailableDrivers(
      final TripDetails tripDetails) {
    final PositionVO startPos = tripDetails.getStartPos();
    return this.driverLocationService
        .findAvailableDrivers(startPos.getLng(), startPos.getLat());
  }

  @Transactional
  public void submitTripAcceptance(final String tripId,
      final DriverAcceptTripDetails details) {
    log.info("Driver {} wants to accept trip {}", details.getDriverId(),
        tripId);
    this.withCurrentDispatch(tripId, dispatch -> {
      this.dispatchRepository.save(dispatch.submitTripAcceptance(details));
      this.tripAcceptanceService.addDriverToAcceptTrip(tripId, details);
    });
  }

  @Transactional
  public void selectTripAcceptance(final String tripId, final String driverId) {
    log.info("Select driver {} to accept trip {}", driverId, tripId);
    this.withCurrentDispatch(tripId, dispatch ->
        this.saveAndPublishEvents(dispatch.selectTripAcceptance(driverId)));
  }

  public Optional<Dispatch> findCurrentDispatchByTrip(final String tripId) {
    return this.dispatchRepository.findCurrentDispatch(tripId);
  }

  private void withCurrentDispatch(final String tripId,
      final Consumer<Dispatch> dispatchConsumer,
      final Runnable noDispatchAction) {
    final Optional<Dispatch> optionalDispatch = this
        .findCurrentDispatchByTrip(tripId);
    if (optionalDispatch.isPresent()) {
      dispatchConsumer.accept(optionalDispatch.get());
    } else {
      log.warn("No active dispatch found for trip {}", tripId);
      noDispatchAction.run();
    }
  }

  private void withCurrentDispatch(final String tripId,
      final Consumer<Dispatch> dispatchConsumer) {
    this.withCurrentDispatch(tripId, dispatchConsumer, () -> {
    });
  }

  @Transactional
  public void notifyTripDispatchFailed(final String tripId,
      final TripDispatchFailedReason reason) {
    log.info("Failed to dispatch trip {} with reason {}", tripId, reason);
    this.withCurrentDispatch(tripId,
        dispatch -> this.saveAndPublishEvents(
            dispatch.markAsFailed(tripId, reason)));
  }

  private void saveAndPublishEvents(
      final ResultWithDomainEvents<Dispatch, DispatchDomainEvent> result) {
    final Dispatch dispatch = result.result;
    this.dispatchRepository.save(dispatch);
    this.eventPublisher.publish(dispatch, result.events);
  }
}

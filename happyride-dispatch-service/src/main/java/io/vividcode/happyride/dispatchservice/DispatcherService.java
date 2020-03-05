package io.vividcode.happyride.dispatchservice;

import io.eventuate.tram.events.aggregates.ResultWithDomainEvents;
import io.vividcode.happyride.common.Position;
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
public class DispatcherService {

  @Autowired
  DriverLocationService driverLocationService;

  @Autowired
  TripAcceptanceService tripAcceptanceService;

  @Autowired
  DispatchRepository dispatchRepository;

  @Autowired
  DispatchDomainEventPublisher eventPublisher;

  private final Duration acceptanceCheckInterval = Duration.ofSeconds(10);

  public void verifyDispatch(TripDetails tripDetails) {
    Position startPos = tripDetails.getStartPos();
    Set<AvailableDriver> availableDrivers = driverLocationService.findAvailableDrivers(
        startPos.getLng(),
        startPos.getLat()
    );
    if (availableDrivers.isEmpty()) {
      throw new DispatchVerificationException();
    }
  }

  @Transactional
  public void dispatchTrip(String tripId, TripDetails tripDetails) {
    Position startPos = tripDetails.getStartPos();
    Set<AvailableDriver> availableDrivers = driverLocationService.findAvailableDrivers(
        startPos.getLng(),
        startPos.getLat()
    );
    saveAndPublishEvents(Dispatch.createDispatch(tripId, tripDetails, availableDrivers));
    tripAcceptanceService.startTripAcceptanceCheck(tripId, tripDetails, acceptanceCheckInterval,
        this::selectTripAcceptance, this::notifyTripDispatchFailed);
    log.info("Dispatched trip {} to drivers {}", tripId, availableDrivers);
  }

  @Transactional
  public void submitTripAcceptance(String tripId, DriverAcceptTripDetails details) {
    log.info("Driver {} wants to accept trip {}", details.getDriverId(), tripId);
    withCurrentDispatch(tripId, dispatch -> {
      dispatchRepository.save(dispatch.submitTripAcceptance(details));
      tripAcceptanceService.addDriverToAcceptTrip(tripId, details);
    });
  }

  @Transactional
  public void selectTripAcceptance(String tripId, String driverId) {
    log.info("Select driver {} to accept trip {}", driverId, tripId);
    withCurrentDispatch(tripId, dispatch ->
        saveAndPublishEvents(dispatch.selectTripAcceptance(driverId)));
  }

  private void withCurrentDispatch(String tripId, Consumer<Dispatch> dispatchConsumer,
      Runnable noDispatchAction) {
    Optional<Dispatch> optionalDispatch = dispatchRepository.findCurrentDispatch(tripId);
    if (optionalDispatch.isPresent()) {
      dispatchConsumer.accept(optionalDispatch.get());
    } else {
      log.warn("No active dispatch found for trip {}", tripId);
      noDispatchAction.run();
    }
  }

  private void withCurrentDispatch(String tripId, Consumer<Dispatch> dispatchConsumer) {
    withCurrentDispatch(tripId, dispatchConsumer, () -> {
    });
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


}

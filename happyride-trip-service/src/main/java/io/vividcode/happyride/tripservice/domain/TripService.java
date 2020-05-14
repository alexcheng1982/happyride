package io.vividcode.happyride.tripservice.domain;

import com.google.common.collect.ImmutableList;
import io.eventuate.tram.events.aggregates.ResultWithDomainEvents;
import io.eventuate.tram.sagas.orchestration.SagaInstanceFactory;
import io.vividcode.happyride.common.PositionVO;
import io.vividcode.happyride.tripservice.api.events.CancellationParty;
import io.vividcode.happyride.tripservice.api.events.DriverAcceptTripDetails;
import io.vividcode.happyride.tripservice.api.events.DriverAcceptTripEvent;
import io.vividcode.happyride.tripservice.api.events.TripDetails;
import io.vividcode.happyride.tripservice.api.events.TripDomainEvent;
import io.vividcode.happyride.tripservice.api.web.TripVO;
import io.vividcode.happyride.tripservice.dataaccess.TripRepository;
import io.vividcode.happyride.tripservice.sagas.createtrip.CreateTripSaga;
import io.vividcode.happyride.tripservice.sagas.createtrip.CreateTripSagaState;
import java.math.BigDecimal;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class TripService {

  @Autowired
  TripRepository tripRepository;

  @Autowired
  TripDomainEventPublisher eventPublisher;

  @Autowired
  SagaInstanceFactory sagaInstanceFactory;

  @Autowired
  CreateTripSaga createTripSaga;

  public TripVO createTrip(final String passengerId, final PositionVO startPos,
      final PositionVO endPos) {
    final ResultWithDomainEvents<Trip, TripDomainEvent> tripAndEvents = Trip
        .createTrip(passengerId, startPos, endPos);
    final Trip trip = tripAndEvents.result;
    this.tripRepository.save(trip);
    this.eventPublisher.publish(trip, tripAndEvents.events);

    final TripDetails tripDetails = new TripDetails(passengerId, startPos,
        endPos);
    final CreateTripSagaState data = new CreateTripSagaState(trip.getId(),
        tripDetails);
    this.sagaInstanceFactory.create(this.createTripSaga, data);
    return trip.toTripVO();
  }

  public Optional<TripVO> getTrip(final String id) {
    return this.tripRepository.findById(id).map(Trip::toTripVO);
  }

  public void markTripAsDispatched(final String tripId) {
    this.updateTrip(tripId, Trip::markAsDispatched);
  }

  public void driverAcceptTrip(final String tripId, final String driverId,
      final BigDecimal posLng,
      final BigDecimal posLat) {
    this.withTrip(tripId, trip -> this.eventPublisher.publish(trip,
        ImmutableList
            .of(new DriverAcceptTripEvent(
                new DriverAcceptTripDetails(driverId, posLng, posLat)))));
  }

  public void markTripAsAccepted(final String tripId, final String driverId) {
    this.updateTrip(tripId, trip -> trip.acceptByDriver(driverId));
  }

  public void markTripAsStarted(final String tripId) {
    this.updateTrip(tripId, Trip::startTrip);
  }

  public void markTripAsFinished(final String tripId) {
    this.updateTrip(tripId, Trip::finishTrip);
  }

  public void markTripAsFailed(final String tripId) {
    this.updateTrip(tripId, Trip::markAsFailed);
  }

  public void rejectTrip(final String tripId) {
    this.updateTrip(tripId, Trip::rejectTrip);
  }

  public void confirmTrip(final String tripId) {
    this.updateTrip(tripId, Trip::confirmTrip);
  }

  public void shouldCancel(final String tripId,
      final CancellationParty initiator) {
    this.updateTrip(tripId, trip -> trip.shouldCancel(initiator));
  }

  public void shouldNotCancel(final String tripId,
      final CancellationParty initiator) {
    this.updateTrip(tripId, trip -> trip.shouldNotCancel(initiator));
  }

  private void updateTrip(final String tripId,
      final Function<Trip, ResultWithDomainEvents<Trip, TripDomainEvent>> updater) {
    this.withTrip(tripId,
        trip -> this.saveAndPublishEvents(updater.apply(trip)));
  }

  private void withTrip(final String tripId, final Consumer<Trip> consumer) {
    this.tripRepository.findById(tripId).ifPresent(consumer);
  }

  private void saveAndPublishEvents(
      final ResultWithDomainEvents<Trip, TripDomainEvent> result) {
    final Trip trip = result.result;
    this.tripRepository.save(trip);
    this.eventPublisher.publish(trip, result.events);
  }
}

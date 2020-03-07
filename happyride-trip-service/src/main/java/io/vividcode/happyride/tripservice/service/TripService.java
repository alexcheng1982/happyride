package io.vividcode.happyride.tripservice.service;

import com.google.common.collect.ImmutableList;
import io.eventuate.tram.events.aggregates.ResultWithDomainEvents;
import io.eventuate.tram.sagas.orchestration.SagaManager;
import io.vividcode.happyride.common.Position;
import io.vividcode.happyride.tripservice.api.events.CancellationParty;
import io.vividcode.happyride.tripservice.api.events.DriverAcceptTripDetails;
import io.vividcode.happyride.tripservice.api.events.DriverAcceptTripEvent;
import io.vividcode.happyride.tripservice.api.events.TripDetails;
import io.vividcode.happyride.tripservice.api.events.TripDomainEvent;
import io.vividcode.happyride.tripservice.dataaccess.TripRepository;
import io.vividcode.happyride.tripservice.domain.Trip;
import io.vividcode.happyride.tripservice.domain.TripDomainEventPublisher;
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
  TripDomainEventPublisher tripAggregateEventPublisher;

  @Autowired
  SagaManager<CreateTripSagaState> createTripSagaManager;

  public Trip createTrip(String passengerId, Position startPos, Position endPos) {
    ResultWithDomainEvents<Trip, TripDomainEvent> tripAndEvents = Trip
        .createTrip(passengerId, startPos, endPos);
    Trip trip = tripAndEvents.result;
    tripRepository.save(trip);
    tripAggregateEventPublisher.publish(trip, tripAndEvents.events);

    TripDetails tripDetails = new TripDetails(passengerId, startPos, endPos);
    CreateTripSagaState data = new CreateTripSagaState(trip.getId(), tripDetails);
    createTripSagaManager.create(data, Trip.class, trip.getId());
    return trip;
  }

  public Optional<Trip> getTrip(String id) {
    return tripRepository.findById(id);
  }

  public void markTripAsDispatched(String tripId) {
    updateTrip(tripId, Trip::markAsDispatched);
  }

  public void driverAcceptTrip(String tripId, String driverId, BigDecimal posLng,
      BigDecimal posLat) {
    withTrip(tripId, trip -> tripAggregateEventPublisher.publish(trip,
        ImmutableList
            .of(new DriverAcceptTripEvent(new DriverAcceptTripDetails(driverId, posLng, posLat)))));
  }

  public void markTripAsAccepted(String tripId, String driverId) {
    updateTrip(tripId, trip -> trip.acceptByDriver(driverId));
  }

  public void markTripAsStarted(String tripId) {
    updateTrip(tripId, Trip::startTrip);
  }

  public void markTripAsFinished(String tripId) {
    updateTrip(tripId, Trip::finishTrip);
  }

  public void rejectTrip(String tripId) {
    updateTrip(tripId, Trip::rejectTrip);
  }

  public void confirmTrip(String tripId) {
    updateTrip(tripId, Trip::confirmTrip);
  }

  public void shouldCancel(String tripId, CancellationParty initiator) {
    updateTrip(tripId, trip -> trip.shouldCancel(initiator));
  }

  public void shouldNotCancel(String tripId, CancellationParty initiator) {
    updateTrip(tripId, trip -> trip.shouldNotCancel(initiator));
  }

  private void updateTrip(String tripId,
      Function<Trip, ResultWithDomainEvents<Trip, TripDomainEvent>> updater) {
    withTrip(tripId, trip -> saveAndPublishEvents(updater.apply(trip)));
  }

  private void withTrip(String tripId, Consumer<Trip> consumer) {
    tripRepository.findById(tripId).ifPresent(consumer);
  }

  private void saveAndPublishEvents(ResultWithDomainEvents<Trip, TripDomainEvent> result) {
    Trip trip = result.result;
    tripRepository.save(trip);
    tripAggregateEventPublisher.publish(trip, result.events);
  }
}

package io.vividcode.happyride.tripservice.service;

import com.google.common.collect.ImmutableList;
import io.eventuate.tram.events.aggregates.ResultWithDomainEvents;
import io.vividcode.happyride.tripservice.api.events.DriverAcceptTripDetails;
import io.vividcode.happyride.tripservice.api.events.DriverAcceptTripEvent;
import io.vividcode.happyride.tripservice.dataaccess.TripRepository;
import io.vividcode.happyride.common.Position;
import io.vividcode.happyride.tripservice.domain.Trip;
import io.vividcode.happyride.tripservice.domain.TripDomainEventPublisher;
import io.vividcode.happyride.tripservice.api.events.TripDomainEvent;
import io.vividcode.happyride.tripservice.domain.TripState;
import java.math.BigDecimal;
import java.util.Optional;
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

  public Trip createTrip(String passengerId, Position startPos, Position endPos) {
    ResultWithDomainEvents<Trip, TripDomainEvent> tripAndEvents = Trip
        .createTrip(passengerId, startPos, endPos);
    Trip trip = tripAndEvents.result;
    tripRepository.save(trip);
    tripAggregateEventPublisher.publish(trip, tripAndEvents.events);
    return trip;
  }

  public Optional<Trip> getRide(String id) {
    return tripRepository.findById(id);
  }

  public void updateTripState(String tripId, TripState state) {
    tripRepository.findById(tripId).ifPresent(trip -> {
      trip.setState(state);
      tripRepository.save(trip);
    });
  }

  public void acceptTrip(String tripId, String driverId, BigDecimal posLng, BigDecimal posLat) {
    tripRepository.findById(tripId).ifPresent(trip -> tripAggregateEventPublisher.publish(trip,
        ImmutableList.of(new DriverAcceptTripEvent(new DriverAcceptTripDetails(driverId, posLng, posLat)))));
  }
}

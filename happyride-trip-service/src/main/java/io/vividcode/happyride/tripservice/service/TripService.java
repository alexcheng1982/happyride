package io.vividcode.happyride.tripservice.service;

import io.eventuate.tram.events.aggregates.ResultWithDomainEvents;
import io.vividcode.happyride.tripservice.dataaccess.TripRepository;
import io.vividcode.happyride.common.Position;
import io.vividcode.happyride.tripservice.domain.Trip;
import io.vividcode.happyride.tripservice.domain.TripDomainEventPublisher;
import io.vividcode.happyride.tripservice.api.events.TripDomainEvent;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class TripService {
  @Autowired
  private TripRepository tripRepository;

  @Autowired
  private TripDomainEventPublisher rideAggregateEventPublisher;

  public Trip createTrip(String passengerId, Position startPos, Position endPos) {
    ResultWithDomainEvents<Trip, TripDomainEvent> tripAndEvents = Trip
        .createTrip(passengerId, startPos, endPos);
    Trip trip = tripAndEvents.result;
    tripRepository.save(trip);
    rideAggregateEventPublisher.publish(trip, tripAndEvents.events);
    return trip;
  }

  public Optional<Trip> getRide(String id) {
    return tripRepository.findById(id);
  }
}

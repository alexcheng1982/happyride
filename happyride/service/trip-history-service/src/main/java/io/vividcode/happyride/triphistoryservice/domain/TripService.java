package io.vividcode.happyride.triphistoryservice.domain;

import io.vividcode.happyride.passengerservice.api.events.PassengerDetails;
import io.vividcode.happyride.triphistoryservice.dataaccess.TripRepository;
import io.vividcode.happyride.tripservice.api.TripState;
import io.vividcode.happyride.tripservice.api.events.TripDetails;
import java.util.List;
import java.util.function.Consumer;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class TripService {

  @Autowired
  TripRepository tripRepository;

  public List<Trip> findByPassenger(final String passengerId) {
    return this.tripRepository.findByPassengerId(passengerId);
  }

  public List<Trip> findByDriver(final String driverId) {
    return this.tripRepository.findByDriverId(driverId);
  }

  public void createTrip(final String tripId, final TripDetails tripDetails) {
    final Trip trip = new Trip();
    trip.setId(tripId);
    trip.setPassengerId(tripDetails.getPassengerId());
    trip.setStartPosLat(tripDetails.getStartPos().getLat());
    trip.setStartPosLng(tripDetails.getStartPos().getLng());
    trip.setEndPosLat(tripDetails.getEndPos().getLat());
    trip.setEndPosLng(tripDetails.getEndPos().getLng());
    trip.setState(TripState.CREATED);
    this.tripRepository.save(trip);
  }

  public void updatePassengerDetails(final String passengerId,
      final PassengerDetails passengerDetails) {
    this.tripRepository
        .updatePassengerDetails(passengerId,
            passengerDetails.getPassengerName());
  }

  public void updateTripState(final String tripId, final TripState state) {
    this.withTrip(tripId, trip -> {
      trip.setState(state);
      this.tripRepository.save(trip);
    });
  }

  public void setTripDriver(final String tripId, final String driverId) {
    this.withTrip(tripId, trip -> {
      trip.setDriverId(driverId);
      this.tripRepository.save(trip);
    });
  }

  private void withTrip(final String tripId, final Consumer<Trip> consumer) {
    this.tripRepository.findById(tripId).ifPresent(consumer);
  }
}

package io.vividcode.happyride.triphistoryservice.dataaccess;

import io.vividcode.happyride.triphistoryservice.domain.Trip;
import java.util.List;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TripRepository extends CrudRepository<Trip, String> {

  List<Trip> findByPassengerId(String passengerId);

  List<Trip> findByDriverId(String driverId);

  @Modifying
  @Query("update Trip t set t.passengerName = :passengerName where t.passengerId = :passengerId")
  int updatePassengerDetails(@Param("passengerId") String passengerId,
      @Param("passengerName") String passengerName);
}

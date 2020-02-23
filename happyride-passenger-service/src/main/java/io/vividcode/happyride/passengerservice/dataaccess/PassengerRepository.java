package io.vividcode.happyride.passengerservice.dataaccess;

import io.vividcode.happyride.passengerservice.domain.Passenger;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PassengerRepository extends CrudRepository<Passenger, String> {

}

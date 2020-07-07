package io.vividcode.happyride.passengerservice.dataaccess;

import io.vividcode.happyride.passengerservice.domain.Passenger;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PassengerRepository extends
    PagingAndSortingRepository<Passenger, String> {

}

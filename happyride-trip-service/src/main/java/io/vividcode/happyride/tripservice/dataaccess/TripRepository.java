package io.vividcode.happyride.tripservice.dataaccess;

import io.vividcode.happyride.tripservice.domain.Trip;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TripRepository extends CrudRepository<Trip, String> {

}

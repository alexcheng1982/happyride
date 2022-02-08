package io.vividcode.happyride.tripservice.cqrs.dataaccess;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TripViewRepository extends CrudRepository<TripView, String> {

}

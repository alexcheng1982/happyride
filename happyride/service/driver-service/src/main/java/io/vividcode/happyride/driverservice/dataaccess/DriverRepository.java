package io.vividcode.happyride.driverservice.dataaccess;

import io.vividcode.happyride.driverservice.model.Driver;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DriverRepository extends CrudRepository<Driver, String> {

}

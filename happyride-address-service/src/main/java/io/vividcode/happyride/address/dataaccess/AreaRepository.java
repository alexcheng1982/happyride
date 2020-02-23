package io.vividcode.happyride.address.dataaccess;

import io.vividcode.happyride.address.domain.Area;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AreaRepository extends CrudRepository<Area, Long> {

}

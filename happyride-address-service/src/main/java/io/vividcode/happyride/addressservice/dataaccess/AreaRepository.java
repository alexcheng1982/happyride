package io.vividcode.happyride.addressservice.dataaccess;

import io.vividcode.happyride.addressservice.domain.Area;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AreaRepository extends CrudRepository<Area, Long> {

  Optional<Area> findByAreaCode(Long areaCode);
}

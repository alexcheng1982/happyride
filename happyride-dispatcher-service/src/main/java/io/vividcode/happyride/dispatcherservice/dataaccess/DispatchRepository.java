package io.vividcode.happyride.dispatcherservice.dataaccess;

import io.vividcode.happyride.dispatcherservice.domain.Dispatch;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DispatchRepository extends CrudRepository<Dispatch, String> {

}

package io.vividcode.happyride.dispatcherservice.dataaccess;

import io.vividcode.happyride.dispatcherservice.domain.Dispatch;
import io.vividcode.happyride.dispatcherservice.domain.DispatchState;
import java.util.List;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DispatchRepository extends CrudRepository<Dispatch, String> {
  List<Dispatch> findByTripIdAndState(String tripId, DispatchState state);

  default Optional<Dispatch> findCurrentDispatch(String tripId) {
    return findByTripIdAndState(tripId, DispatchState.WAIT_FOR_ACCEPTANCE).stream().findFirst();
  }
}

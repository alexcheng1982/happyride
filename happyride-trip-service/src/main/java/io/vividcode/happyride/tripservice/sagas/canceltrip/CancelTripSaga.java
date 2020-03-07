package io.vividcode.happyride.tripservice.sagas.canceltrip;

import io.eventuate.tram.sagas.orchestration.SagaDefinition;
import io.eventuate.tram.sagas.simpledsl.SimpleSaga;

public class CancelTripSaga implements SimpleSaga<CancelTripSagaState> {
  private SagaDefinition<CancelTripSagaState> sagaDefinition;

  @Override
  public SagaDefinition<CancelTripSagaState> getSagaDefinition() {
    return sagaDefinition;
  }
}

package io.vividcode.happyride.tripservice.sagas.createtrip;

import io.eventuate.tram.sagas.orchestration.SagaDefinition;
import io.eventuate.tram.sagas.simpledsl.SimpleSaga;
import io.vividcode.happyride.tripservice.sagaparticipants.DispatchServiceProxy;
import io.vividcode.happyride.tripservice.sagaparticipants.TripServiceProxy;
import io.vividcode.happyride.tripservice.sagaparticipants.TripValidationServiceProxy;
import org.springframework.stereotype.Component;

@Component
public class CreateTripSaga implements SimpleSaga<CreateTripSagaState> {

  private SagaDefinition<CreateTripSagaState> sagaDefinition;

  public CreateTripSaga(TripServiceProxy tripService,
      TripValidationServiceProxy tripValidationService,
      DispatchServiceProxy dispatchService) {
    sagaDefinition = step()
        .withCompensation(tripService.reject, CreateTripSagaState::createRejectTripCommand)
        .step()
        .invokeParticipant(tripValidationService.validateTrip,
            CreateTripSagaState::createValidateTripCommand)
        .step()
        .invokeParticipant(dispatchService.verifyDispatch,
            CreateTripSagaState::createVerifyDispatchCommand)
        .step()
        .invokeParticipant(tripService.confirm, CreateTripSagaState::createConfirmTripCommand)
        .build();
  }

  @Override
  public SagaDefinition<CreateTripSagaState> getSagaDefinition() {
    return sagaDefinition;
  }
}

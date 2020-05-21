package io.vividcode.happyride.tripservice.sagas.createtrip;

import io.eventuate.tram.commands.common.Success;
import io.eventuate.tram.sagas.orchestration.SagaDefinition;
import io.eventuate.tram.sagas.simpledsl.SimpleSaga;
import io.vividcode.happyride.paymentservice.api.events.PaymentFailedReply;
import io.vividcode.happyride.tripservice.sagaparticipants.PaymentServiceProxy;
import io.vividcode.happyride.tripservice.sagaparticipants.TripServiceProxy;
import io.vividcode.happyride.tripservice.sagaparticipants.TripValidationServiceProxy;
import java.util.function.Predicate;
import org.springframework.stereotype.Component;

@Component
public class CreateTripSaga implements SimpleSaga<CreateTripSagaState> {

  private final SagaDefinition<CreateTripSagaState> sagaDefinition;

  public CreateTripSaga(final TripServiceProxy tripService,
      final TripValidationServiceProxy tripValidationService,
      final PaymentServiceProxy paymentService) {
    this.sagaDefinition = this.step()
        .withCompensation(tripService.reject,
            CreateTripSagaState::createRejectTripCommand)
        .step()
        .invokeParticipant(tripValidationService.validateTrip,
            CreateTripSagaState::createValidateTripCommand)
        .step()
        .invokeParticipant(paymentService.createPayment,
            CreateTripSagaState::createPaymentCommand)
        .step()
        .invokeParticipant(
            CreateTripSagaState::paymentRequired, paymentService.makePayment,
            CreateTripSagaState::makePaymentCommand)
        .onReply(PaymentFailedReply.class,
            CreateTripSagaState::handlePaymentFailedReply)
        .onReply(Success.class, (state, success) -> state.markAsPaid())
        .step()
        .invokeParticipant(CreateTripSagaState::shouldConfirmTrip,
            tripService.confirm,
            CreateTripSagaState::createConfirmTripCommand)
        .step()
        .invokeParticipant(
            ((Predicate<CreateTripSagaState>) CreateTripSagaState::shouldConfirmTrip)
                .negate(),
            tripService.reject, CreateTripSagaState::createRejectTripCommand)
        .build();
  }

  @Override
  public SagaDefinition<CreateTripSagaState> getSagaDefinition() {
    return this.sagaDefinition;
  }
}

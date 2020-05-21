package io.vividcode.happyride.tripservice.sagas.createtrip;

import static io.eventuate.tram.sagas.testing.SagaUnitTestSupport.given;

import io.vividcode.happyride.common.PositionVO;
import io.vividcode.happyride.dispatchservice.api.DispatchServiceChannels;
import io.vividcode.happyride.dispatchservice.api.events.VerifyDispatchCommand;
import io.vividcode.happyride.tripservice.api.TripServiceChannels;
import io.vividcode.happyride.tripservice.api.events.TripDetails;
import io.vividcode.happyride.tripservice.sagaparticipants.ConfirmTripCommand;
import io.vividcode.happyride.tripservice.sagaparticipants.PaymentServiceProxy;
import io.vividcode.happyride.tripservice.sagaparticipants.RejectTripCommand;
import io.vividcode.happyride.tripservice.sagaparticipants.TripServiceProxy;
import io.vividcode.happyride.tripservice.sagaparticipants.TripValidationServiceProxy;
import io.vividcode.happyride.tripservice.sagas.createtrip.CreateTripSagaTest.TestConfig;
import io.vividcode.happyride.tripvalidationservice.api.TripValidationServiceChannels;
import io.vividcode.happyride.tripvalidationservice.api.ValidateTripCommand;
import java.math.BigDecimal;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestConfig.class)
@DisplayName("Trip saga")
public class CreateTripSagaTest {

  @Autowired
  TripServiceProxy tripService;

  @Autowired
  TripValidationServiceProxy tripValidationService;

  @Autowired
  PaymentServiceProxy paymentService;

  @Test
  @DisplayName("Create trip")
  public void shouldCreateTrip() {
    final String tripId = this.uuid();
    final TripDetails tripDetails = this.tripDetails0();
    given().saga(this.makeCreateTripSaga(),
        new CreateTripSagaState(tripId, tripDetails, BigDecimal.valueOf(50)))
        .expect().command(new ValidateTripCommand(tripDetails))
        .to(TripValidationServiceChannels.tripValidation)
        .andGiven().successReply()
        .expect()
        .command(new VerifyDispatchCommand(tripDetails))
        .to(DispatchServiceChannels.dispatchServiceChannel)
        .andGiven().successReply()
        .expect()
        .command(new ConfirmTripCommand(tripId))
        .to(TripServiceChannels.tripServiceChannel);
  }

  @Test
  @DisplayName("Reject trip due to validation failed")
  public void shouldRejectTripDueToTripValidationFailed() {
    final String tripId = this.uuid();
    final TripDetails tripDetails = this.tripDetails0();
    given().saga(this.makeCreateTripSaga(),
        new CreateTripSagaState(tripId, tripDetails, BigDecimal.valueOf(50)))
        .expect().command(new ValidateTripCommand(tripDetails))
        .to(TripValidationServiceChannels.tripValidation)
        .andGiven().failureReply()
        .expect().command(new RejectTripCommand(tripId))
        .to(TripServiceChannels.tripServiceChannel);
  }

  @Test
  @DisplayName("Reject trip due to dispatch verification failed")
  public void shouldRejectTripDueToDispatchVerificationFailed() {
    final String tripId = this.uuid();
    final TripDetails tripDetails = this.tripDetails0();
    given().saga(this.makeCreateTripSaga(),
        new CreateTripSagaState(tripId, tripDetails, BigDecimal.valueOf(50)))
        .expect().command(new ValidateTripCommand(tripDetails))
        .to(TripValidationServiceChannels.tripValidation)
        .andGiven().successReply()
        .expect().command(new VerifyDispatchCommand(tripDetails))
        .to(DispatchServiceChannels.dispatchServiceChannel)
        .andGiven().failureReply()
        .expect()
        .command(new RejectTripCommand(tripId))
        .to(TripServiceChannels.tripServiceChannel);
  }

  private CreateTripSaga makeCreateTripSaga() {
    return new CreateTripSaga(this.tripService, this.tripValidationService,
        this.paymentService);
  }

  private TripDetails tripDetails0() {
    return new TripDetails(this.uuid(),
        new PositionVO(BigDecimal.ZERO, BigDecimal.ZERO),
        new PositionVO(BigDecimal.ZERO, BigDecimal.ZERO));
  }

  private String uuid() {
    return UUID.randomUUID().toString();
  }

  @TestConfiguration
  @ComponentScan(basePackageClasses = TripServiceProxy.class)
  static class TestConfig {

  }
}

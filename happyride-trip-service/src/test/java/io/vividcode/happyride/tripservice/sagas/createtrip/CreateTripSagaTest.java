package io.vividcode.happyride.tripservice.sagas.createtrip;

import static io.eventuate.tram.sagas.testing.SagaUnitTestSupport.given;

import io.vividcode.happyride.common.Position;
import io.vividcode.happyride.common.PositionView;
import io.vividcode.happyride.dispatchservice.api.DispatchServiceChannels;
import io.vividcode.happyride.dispatchservice.api.events.VerifyDispatchCommand;
import io.vividcode.happyride.tripservice.api.TripServiceChannels;
import io.vividcode.happyride.tripservice.api.events.TripDetails;
import io.vividcode.happyride.tripservice.sagaparticipants.ConfirmTripCommand;
import io.vividcode.happyride.tripservice.sagaparticipants.DispatchServiceProxy;
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
@DisplayName("行程Saga")
public class CreateTripSagaTest {

  @Autowired
  TripServiceProxy tripService;

  @Autowired
  TripValidationServiceProxy tripValidationService;

  @Autowired
  DispatchServiceProxy dispatchService;

  @Test
  @DisplayName("正确的行程创建")
  public void shouldCreateTrip() {
    String tripId = uuid();
    TripDetails tripDetails = tripDetails0();
    given().saga(makeCreateTripSaga(), new CreateTripSagaState(tripId, tripDetails))
        .expect().command(new ValidateTripCommand(tripDetails))
        .to(TripValidationServiceChannels.tripValidationServiceChannel)
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
  @DisplayName("行程校验失败时拒绝行程")
  public void shouldRejectTripDueToTripValidationFailed() {
    String tripId = uuid();
    TripDetails tripDetails = tripDetails0();
    given().saga(makeCreateTripSaga(), new CreateTripSagaState(tripId, tripDetails))
        .expect().command(new ValidateTripCommand(tripDetails))
        .to(TripValidationServiceChannels.tripValidationServiceChannel)
        .andGiven().failureReply()
        .expect().command(new RejectTripCommand(tripId))
        .to(TripServiceChannels.tripServiceChannel);
  }

  @Test
  @DisplayName("派发验证失败时拒绝行程")
  public void shouldRejectTripDueToDispatchVerificationFailed() {
    String tripId = uuid();
    TripDetails tripDetails = tripDetails0();
    given().saga(makeCreateTripSaga(), new CreateTripSagaState(tripId, tripDetails))
        .expect().command(new ValidateTripCommand(tripDetails))
        .to(TripValidationServiceChannels.tripValidationServiceChannel)
        .andGiven().successReply()
        .expect().command(new VerifyDispatchCommand(tripDetails))
        .to(DispatchServiceChannels.dispatchServiceChannel)
        .andGiven().failureReply()
        .expect()
        .command(new RejectTripCommand(tripId)).to(TripServiceChannels.tripServiceChannel);
  }

  private CreateTripSaga makeCreateTripSaga() {
    return new CreateTripSaga(tripService, tripValidationService, dispatchService);
  }

  private TripDetails tripDetails0() {
    return new TripDetails(uuid(), new PositionView(BigDecimal.ZERO, BigDecimal.ZERO),
        new PositionView(BigDecimal.ZERO, BigDecimal.ZERO));
  }

  private String uuid() {
    return UUID.randomUUID().toString();
  }

  @TestConfiguration
  @ComponentScan(basePackageClasses = TripServiceProxy.class)
  static class TestConfig {

  }
}

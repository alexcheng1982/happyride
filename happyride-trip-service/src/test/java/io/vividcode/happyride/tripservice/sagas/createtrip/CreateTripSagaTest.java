package io.vividcode.happyride.tripservice.sagas.createtrip;

import io.vividcode.happyride.common.PositionVO;
import io.vividcode.happyride.paymentservice.api.PaymentServiceChannels;
import io.vividcode.happyride.paymentservice.api.events.CreatePaymentCommand;
import io.vividcode.happyride.paymentservice.api.events.MakePaymentCommand;
import io.vividcode.happyride.paymentservice.api.events.PaymentFailedReply;
import io.vividcode.happyride.tripservice.api.TripServiceChannels;
import io.vividcode.happyride.tripservice.api.events.TripDetails;
import io.vividcode.happyride.tripservice.sagaparticipants.*;
import io.vividcode.happyride.tripservice.sagas.createtrip.CreateTripSagaTest.TestConfig;
import io.vividcode.happyride.tripvalidationservice.api.TripValidationServiceChannels;
import io.vividcode.happyride.tripvalidationservice.api.ValidateTripCommand;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.util.UUID;

import static io.eventuate.tram.sagas.testing.SagaUnitTestSupport.given;

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
    final BigDecimal fare = BigDecimal.valueOf(50);
    given().saga(this.makeCreateTripSaga(),
        new CreateTripSagaState(tripId, tripDetails, fare))
        .expect().command(new ValidateTripCommand(tripDetails))
        .to(TripValidationServiceChannels.tripValidation)
        .andGiven().successReply()
        .expect().command(new CreatePaymentCommand(tripId, fare))
        .to(PaymentServiceChannels.payment).andGiven().successReply()
        .expect()
        .command(new ConfirmTripCommand(tripId))
        .to(TripServiceChannels.trip);
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
        .to(TripServiceChannels.trip);
  }

  @Test
  @DisplayName("Reject trip due to payment failed")
  public void shouldRejectTripDueToPaymentFailed() {
    final String tripId = this.uuid();
    final TripDetails tripDetails = this.tripDetails0();
    final BigDecimal fare = BigDecimal.valueOf(250);
    given().saga(this.makeCreateTripSaga(),
        new CreateTripSagaState(tripId, tripDetails, fare))
        .expect().command(new ValidateTripCommand(tripDetails))
        .to(TripValidationServiceChannels.tripValidation)
        .andGiven().successReply()
        .expect().command(new CreatePaymentCommand(tripId, fare))
        .to(PaymentServiceChannels.payment)
        .andGiven().successReply()
        .expect().command(new MakePaymentCommand(tripId))
        .to(PaymentServiceChannels.payment)
        .andGiven().failureReply(new PaymentFailedReply("error"))
        .expect()
        .command(new RejectTripCommand(tripId))
        .to(TripServiceChannels.trip);
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

package io.vividcode.happyride.tripservice.sagas.createtrip;

import io.vividcode.happyride.paymentservice.api.events.CreatePaymentCommand;
import io.vividcode.happyride.paymentservice.api.events.MakePaymentCommand;
import io.vividcode.happyride.paymentservice.api.events.PaymentFailedReply;
import io.vividcode.happyride.tripservice.api.events.TripDetails;
import io.vividcode.happyride.tripservice.sagaparticipants.ConfirmTripCommand;
import io.vividcode.happyride.tripservice.sagaparticipants.RejectTripCommand;
import io.vividcode.happyride.tripvalidationservice.api.ValidateTripCommand;
import java.math.BigDecimal;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@NoArgsConstructor
@RequiredArgsConstructor
public class CreateTripSagaState {

  @NonNull
  private String tripId;

  @NonNull
  private TripDetails tripDetails;

  @NonNull
  private BigDecimal fare;

  private boolean paid;

  public RejectTripCommand createRejectTripCommand() {
    return new RejectTripCommand(this.tripId);
  }

  public ValidateTripCommand createValidateTripCommand() {
    return new ValidateTripCommand(this.tripDetails);
  }

  public ConfirmTripCommand createConfirmTripCommand() {
    return new ConfirmTripCommand(this.tripId);
  }

  public CreatePaymentCommand createPaymentCommand() {
    return new CreatePaymentCommand(this.tripId, this.fare);
  }

  public MakePaymentCommand makePaymentCommand() {
    return new MakePaymentCommand(this.tripId);
  }

  public boolean paymentRequired() {
    return this.fare.compareTo(BigDecimal.valueOf(100)) > 0;
  }

  public void markAsPaid() {
    this.setPaid(true);
  }

  public void handlePaymentFailedReply(final PaymentFailedReply reply) {
    this.setPaid(false);
  }

  public boolean shouldConfirmTrip() {
    return !this.paymentRequired() || (this.paymentRequired() && this.isPaid());
  }
}

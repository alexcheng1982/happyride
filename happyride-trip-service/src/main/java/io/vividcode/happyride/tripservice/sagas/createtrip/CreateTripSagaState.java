package io.vividcode.happyride.tripservice.sagas.createtrip;

import io.vividcode.happyride.dispatcherservice.api.events.VerifyDispatchCommand;
import io.vividcode.happyride.tripservice.api.events.TripDetails;
import io.vividcode.happyride.tripservice.sagaparticipants.ConfirmTripCommand;
import io.vividcode.happyride.tripservice.sagaparticipants.RejectTripCommand;
import io.vividcode.happyride.tripvalidationservice.api.ValidateTripCommand;
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

  public RejectTripCommand createRejectTripCommand() {
    return new RejectTripCommand(tripId);
  }

  public ValidateTripCommand createValidateTripCommand() {
    return new ValidateTripCommand(tripDetails);
  }

  public ConfirmTripCommand createConfirmTripCommand() {
    return new ConfirmTripCommand(tripId);
  }

  public VerifyDispatchCommand createVerifyDispatchCommand() {
    return new VerifyDispatchCommand(tripDetails);
  }
}

package io.vividcode.happyride.tripservice.sagaparticipants;

public class ConfirmTripCommand extends TripCommand {

  public ConfirmTripCommand() {
  }

  public ConfirmTripCommand(String tripId) {
    super(tripId);
  }
}

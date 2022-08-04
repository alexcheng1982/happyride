package io.vividcode.happyride.tripservice.sagaparticipants;

public class RejectTripCommand extends TripCommand {

  public RejectTripCommand() {

  }

  public RejectTripCommand(String tripId) {
    super(tripId);
  }
}

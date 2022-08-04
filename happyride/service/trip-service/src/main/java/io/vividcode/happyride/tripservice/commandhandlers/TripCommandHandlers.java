package io.vividcode.happyride.tripservice.commandhandlers;


import io.eventuate.tram.commands.consumer.CommandHandlers;
import io.eventuate.tram.commands.consumer.CommandMessage;
import io.eventuate.tram.messaging.common.Message;
import io.eventuate.tram.sagas.participant.SagaCommandHandlersBuilder;
import io.vividcode.happyride.tripservice.api.TripServiceChannels;
import io.vividcode.happyride.tripservice.domain.TripService;
import io.vividcode.happyride.tripservice.sagaparticipants.ConfirmTripCommand;
import io.vividcode.happyride.tripservice.sagaparticipants.RejectTripCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static io.eventuate.tram.commands.consumer.CommandHandlerReplyBuilder.withSuccess;

@Component
public class TripCommandHandlers {

  @Autowired
  TripService tripService;

  public CommandHandlers commandHandlers() {
    return SagaCommandHandlersBuilder
        .fromChannel(TripServiceChannels.trip)
        .onMessage(RejectTripCommand.class, this::rejectTrip)
        .onMessage(ConfirmTripCommand.class, this::confirmTrip)
        .build();
  }

  private Message rejectTrip(final CommandMessage<RejectTripCommand> cm) {
    this.tripService.rejectTrip(cm.getCommand().getTripId());
    return withSuccess();
  }

  private Message confirmTrip(final CommandMessage<ConfirmTripCommand> cm) {
    this.tripService.confirmTrip(cm.getCommand().getTripId());
    return withSuccess();
  }
}

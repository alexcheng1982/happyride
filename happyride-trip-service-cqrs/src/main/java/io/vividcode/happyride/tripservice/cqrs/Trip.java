package io.vividcode.happyride.tripservice.cqrs;

import io.vividcode.happyride.common.Position;
import io.vividcode.happyride.tripservice.api.TripState;
import io.vividcode.happyride.tripservice.cqrs.api.CancelTripCommand;
import io.vividcode.happyride.tripservice.cqrs.api.ConfirmTripCommand;
import io.vividcode.happyride.tripservice.cqrs.api.CreateTripCommand;
import io.vividcode.happyride.tripservice.cqrs.api.TripCancelledEvent;
import io.vividcode.happyride.tripservice.cqrs.api.TripConfirmedEvent;
import io.vividcode.happyride.tripservice.cqrs.api.TripCreatedEvent;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.spring.stereotype.Aggregate;
import org.springframework.web.bind.annotation.PostMapping;

import static org.axonframework.modelling.command.AggregateLifecycle.apply;

@Aggregate
public class Trip {
  @AggregateIdentifier
  private String id;

  private Position startPos;

  private Position endPos;

  private TripState state;

  @CommandHandler
  public Trip(CreateTripCommand command) {
    apply(new TripCreatedEvent(command.getTripId(), command.getTripDetails()));
  }

  @CommandHandler
  public void handle(CancelTripCommand command) {
    apply(new TripCancelledEvent(command.getTripId()));
  }

  @CommandHandler
  public void handle(ConfirmTripCommand command) {
    apply(new TripConfirmedEvent(command.getTripId()));
  }

  @EventSourcingHandler
  public void on(TripCreatedEvent event) {
    id = event.getTripId();
    startPos = event.getTripDetails().getStartPos();
    endPos = event.getTripDetails().getEndPos();
  }

  @EventSourcingHandler
  public void on(TripCancelledEvent event) {
    state = TripState.CANCELLED;
  }

  @EventSourcingHandler
  public void on(TripConfirmedEvent event) {
    state = TripState.CONFIRMED;
  }

  protected Trip() {

  }
}

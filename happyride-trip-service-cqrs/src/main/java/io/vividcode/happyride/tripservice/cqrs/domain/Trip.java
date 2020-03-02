package io.vividcode.happyride.tripservice.cqrs.domain;

import static org.axonframework.modelling.command.AggregateLifecycle.apply;

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

@Aggregate
public class Trip {
  @AggregateIdentifier
  private String id;

  private TripState state;

  @CommandHandler
  public Trip(CreateTripCommand command) {
    apply(new TripCreatedEvent(command.getTripId(), command.getTripDetails()));
  }

  @CommandHandler
  public void handle(CancelTripCommand command) {
    if (state != TripState.CREATED) {
      throw new IllegalTripStateException(state, TripState.CANCELLED);
    }
    apply(new TripCancelledEvent(command.getTripId()));
  }

  @CommandHandler
  public void handle(ConfirmTripCommand command) {
    if (state != TripState.CREATED) {
      throw new IllegalTripStateException(state, TripState.CONFIRMED);
    }
    apply(new TripConfirmedEvent(command.getTripId()));
  }

  @EventSourcingHandler
  public void on(TripCreatedEvent event) {
    id = event.getTripId();
    state = TripState.CREATED;
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

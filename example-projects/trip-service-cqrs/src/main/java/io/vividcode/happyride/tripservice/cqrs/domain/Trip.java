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
  public Trip(final CreateTripCommand command) {
    apply(new TripCreatedEvent(command.getTripId(), command.getTripDetails()));
  }

  @CommandHandler
  public void handle(final CancelTripCommand command) {
    if (this.state != TripState.CREATED) {
      throw new IllegalTripStateException(this.state, TripState.CANCELLED);
    }
    apply(new TripCancelledEvent(command.getTripId()));
  }

  @CommandHandler
  public void handle(final ConfirmTripCommand command) {
    if (this.state != TripState.CREATED) {
      throw new IllegalTripStateException(this.state, TripState.CONFIRMED);
    }
    apply(new TripConfirmedEvent(command.getTripId()));
  }

  @EventSourcingHandler
  public void on(final TripCreatedEvent event) {
    this.id = event.getTripId();
    this.state = TripState.CREATED;
  }

  @EventSourcingHandler
  public void on(final TripCancelledEvent event) {
    this.state = TripState.CANCELLED;
  }

  @EventSourcingHandler
  public void on(final TripConfirmedEvent event) {
    this.state = TripState.CONFIRMED;
  }

  protected Trip() {

  }
}

package io.vividcode.happyride.tripservice.domain;

import io.eventuate.tram.events.aggregates.ResultWithDomainEvents;
import io.vividcode.happyride.common.BaseEntityWithGeneratedId;
import io.vividcode.happyride.common.Position;
import io.vividcode.happyride.common.PositionVO;
import io.vividcode.happyride.tripservice.api.TripState;
import io.vividcode.happyride.tripservice.api.events.CancellationParty;
import io.vividcode.happyride.tripservice.api.events.TripCancellationResolutionRequiredEvent;
import io.vividcode.happyride.tripservice.api.events.TripCancelledEvent;
import io.vividcode.happyride.tripservice.api.events.TripConfirmedEvent;
import io.vividcode.happyride.tripservice.api.events.TripCreatedEvent;
import io.vividcode.happyride.tripservice.api.events.TripDetails;
import io.vividcode.happyride.tripservice.api.events.TripDomainEvent;
import io.vividcode.happyride.tripservice.api.events.TripFinishedEvent;
import io.vividcode.happyride.tripservice.api.events.TripRejectedEvent;
import io.vividcode.happyride.tripservice.api.events.TripStartedEvent;
import io.vividcode.happyride.tripservice.api.web.TripVO;
import io.vividcode.happyride.tripservice.service.IllegalTripStateException;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "trips")
@Getter
@Setter
@NoArgsConstructor
@ToString
public class Trip extends BaseEntityWithGeneratedId {

  public static ResultWithDomainEvents<Trip, TripDomainEvent> createTrip(String passengerId,
      PositionVO startPos, PositionVO endPos) {
    Trip trip = new Trip(passengerId, startPos, endPos);
    TripCreatedEvent event = new TripCreatedEvent(new TripDetails(passengerId, startPos, endPos));
    return new ResultWithDomainEvents<>(trip, event);
  }

  @Column(name = "passenger_id")
  private String passengerId;

  @Column(name = "driver_id")
  private String driverId;

  @Embedded
  @AttributeOverrides({
      @AttributeOverride(name = "lat", column = @Column(name = "start_pos_lat")),
      @AttributeOverride(name = "lng", column = @Column(name = "start_pos_lng")),
      @AttributeOverride(name = "addressId", column = @Column(name = "start_pos_address_id"))
  })
  private Position startPos;

  @Embedded
  @AttributeOverrides({
      @AttributeOverride(name = "lat", column = @Column(name = "end_pos_lat")),
      @AttributeOverride(name = "lng", column = @Column(name = "end_pos_lng")),
      @AttributeOverride(name = "addressId", column = @Column(name = "end_pos_address_id"))
  })
  private Position endPos;

  @Column(name = "state")
  @Enumerated(EnumType.STRING)
  private TripState state;

  public Trip(String passengerId, PositionVO startPos, PositionVO endPos) {
    this.passengerId = passengerId;
    this.startPos = startPos.deserialize();
    this.endPos = endPos.deserialize();
    this.state = TripState.CREATED;
  }

  public ResultWithDomainEvents<Trip, TripDomainEvent> markAsDispatched() {
    assertTripState(TripState.CONFIRMED);
    setState(TripState.DISPATCHED);
    return new ResultWithDomainEvents<>(this);
  }

  public ResultWithDomainEvents<Trip, TripDomainEvent> markAsFailed() {
    setState(TripState.FAILED);
    return new ResultWithDomainEvents<>(this);
  }

  public ResultWithDomainEvents<Trip, TripDomainEvent> rejectTrip() {
    setState(TripState.REJECTED);
    return new ResultWithDomainEvents<>(this, new TripRejectedEvent());
  }

  public ResultWithDomainEvents<Trip, TripDomainEvent> confirmTrip() {
    setState(TripState.CONFIRMED);
    TripDetails tripDetails = new TripDetails(passengerId, startPos.serialize(), endPos.serialize());
    return new ResultWithDomainEvents<>(this, new TripConfirmedEvent(tripDetails));
  }

  public ResultWithDomainEvents<Trip, TripDomainEvent> acceptByDriver(String driverId) {
    setDriverId(driverId);
    setState(TripState.ACCEPTED);
    return new ResultWithDomainEvents<>(this);
  }

  public ResultWithDomainEvents<Trip, TripDomainEvent> shouldCancel(
      CancellationParty initiator) {
    List<TripDomainEvent> events = new ArrayList<>();
    if (initiator == CancellationParty.DRIVER) {
      if (state == TripState.CANCELLED_BY_PASSENGER) {
        setState(TripState.CANCELLED);
        events.add(new TripCancelledEvent());
      } else {
        setState(TripState.CANCELLED_BY_DRIVER);
      }
    } else {
      if (state == TripState.CANCELLED_BY_DRIVER) {
        setState(TripState.CANCELLED);
        events.add(new TripCancelledEvent());
      } else {
        setState(TripState.CANCELLED_BY_PASSENGER);
      }
    }
    return new ResultWithDomainEvents<>(this, events.toArray(new TripDomainEvent[0]));
  }

  public ResultWithDomainEvents<Trip, TripDomainEvent> shouldNotCancel(CancellationParty initiator) {
    List<TripDomainEvent> events = new ArrayList<>();
    if (initiator == CancellationParty.DRIVER) {
      if (state == TripState.CANCELLED_BY_PASSENGER) {
        setState(TripState.CANCELLATION_REJECTED_BY_DRIVER);
        events.add(new TripCancellationResolutionRequiredEvent(initiator));
      }
    } else {
      if (state == TripState.CANCELLED_BY_DRIVER) {
        setState(TripState.CANCELLATION_REJECTED_BY_PASSENGER);
        events.add(new TripCancellationResolutionRequiredEvent(initiator));
      }
    }
    return new ResultWithDomainEvents<>(this, events.toArray(new TripDomainEvent[0]));
  }

  public ResultWithDomainEvents<Trip, TripDomainEvent> startTrip() {
    assertTripState(TripState.ACCEPTED);
    setState(TripState.STARTED);
    return new ResultWithDomainEvents<>(this, new TripStartedEvent());
  }

  public ResultWithDomainEvents<Trip, TripDomainEvent> finishTrip() {
    assertTripState(TripState.STARTED);
    setState(TripState.FINISHED);
    return new ResultWithDomainEvents<>(this, new TripFinishedEvent());
  }

  private void assertTripState(TripState requiredState) {
    if (state != requiredState) {
      throw new IllegalTripStateException(requiredState);
    }
  }

  public TripVO serialize() {
    TripVO tripVO = new TripVO();
    tripVO.setId(getId());
    tripVO.setPassengerId(getPassengerId());
    tripVO.setDriverId(getDriverId());
    tripVO.setStartPos(getStartPos().serialize());
    tripVO.setEndPos(getEndPos().serialize());
    tripVO.setState(getState().name());
    return tripVO;
  }
}

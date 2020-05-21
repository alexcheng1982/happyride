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
import java.math.BigDecimal;
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
import javax.validation.constraints.Size;
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

  public static ResultWithDomainEvents<Trip, TripDomainEvent> createTrip(
      final String passengerId,
      final PositionVO startPos, final PositionVO endPos) {
    final Trip trip = new Trip(passengerId, startPos, endPos);
    final TripCreatedEvent event = new TripCreatedEvent(
        new TripDetails(passengerId, startPos, endPos));
    return new ResultWithDomainEvents<>(trip, event);
  }

  @Column(name = "passenger_id")
  @Size(max = 36)
  private String passengerId;

  @Column(name = "driver_id")
  @Size(max = 36)
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

  @Column(name = "fare")
  private BigDecimal fare;

  @Column(name = "state")
  @Enumerated(EnumType.STRING)
  private TripState state;

  public Trip(
      final String passengerId, final PositionVO startPos,
      final PositionVO endPos) {
    this.passengerId = passengerId;
    this.startPos = startPos.deserialize();
    this.endPos = endPos.deserialize();
    this.state = TripState.CREATED;
  }

  public ResultWithDomainEvents<Trip, TripDomainEvent> markAsDispatched() {
    this.assertTripState(TripState.CONFIRMED);
    this.setState(TripState.DISPATCHED);
    return new ResultWithDomainEvents<>(this);
  }

  public ResultWithDomainEvents<Trip, TripDomainEvent> markAsFailed() {
    this.setState(TripState.FAILED);
    return new ResultWithDomainEvents<>(this);
  }

  public ResultWithDomainEvents<Trip, TripDomainEvent> rejectTrip() {
    this.setState(TripState.REJECTED);
    return new ResultWithDomainEvents<>(this, new TripRejectedEvent());
  }

  public ResultWithDomainEvents<Trip, TripDomainEvent> confirmTrip() {
    this.setState(TripState.CONFIRMED);
    final TripDetails tripDetails = new TripDetails(this.passengerId,
        this.startPos.serialize(),
        this.endPos.serialize());
    return new ResultWithDomainEvents<>(this,
        new TripConfirmedEvent(tripDetails));
  }

  public ResultWithDomainEvents<Trip, TripDomainEvent> acceptByDriver(
      final String driverId) {
    this.setDriverId(driverId);
    this.setState(TripState.ACCEPTED);
    return new ResultWithDomainEvents<>(this);
  }

  public ResultWithDomainEvents<Trip, TripDomainEvent> shouldCancel(
      final CancellationParty initiator) {
    final List<TripDomainEvent> events = new ArrayList<>();
    if (initiator == CancellationParty.DRIVER) {
      if (this.state == TripState.CANCELLED_BY_PASSENGER) {
        this.setState(TripState.CANCELLED);
        events.add(new TripCancelledEvent());
      } else {
        this.setState(TripState.CANCELLED_BY_DRIVER);
      }
    } else {
      if (this.state == TripState.CANCELLED_BY_DRIVER) {
        this.setState(TripState.CANCELLED);
        events.add(new TripCancelledEvent());
      } else {
        this.setState(TripState.CANCELLED_BY_PASSENGER);
      }
    }
    return new ResultWithDomainEvents<>(this,
        events.toArray(new TripDomainEvent[0]));
  }

  public ResultWithDomainEvents<Trip, TripDomainEvent> shouldNotCancel(
      final CancellationParty initiator) {
    final List<TripDomainEvent> events = new ArrayList<>();
    if (initiator == CancellationParty.DRIVER) {
      if (this.state == TripState.CANCELLED_BY_PASSENGER) {
        this.setState(TripState.CANCELLATION_REJECTED_BY_DRIVER);
        events.add(new TripCancellationResolutionRequiredEvent(initiator));
      }
    } else {
      if (this.state == TripState.CANCELLED_BY_DRIVER) {
        this.setState(TripState.CANCELLATION_REJECTED_BY_PASSENGER);
        events.add(new TripCancellationResolutionRequiredEvent(initiator));
      }
    }
    return new ResultWithDomainEvents<>(this,
        events.toArray(new TripDomainEvent[0]));
  }

  public ResultWithDomainEvents<Trip, TripDomainEvent> startTrip() {
    this.assertTripState(TripState.ACCEPTED);
    this.setState(TripState.STARTED);
    return new ResultWithDomainEvents<>(this, new TripStartedEvent());
  }

  public ResultWithDomainEvents<Trip, TripDomainEvent> finishTrip() {
    this.assertTripState(TripState.STARTED);
    this.setState(TripState.FINISHED);
    return new ResultWithDomainEvents<>(this, new TripFinishedEvent());
  }

  private void assertTripState(final TripState requiredState) {
    if (this.state != requiredState) {
      throw new IllegalTripStateException(requiredState);
    }
  }

  public TripVO toTripVO() {
    final TripVO tripVO = new TripVO();
    tripVO.setId(this.getId());
    tripVO.setPassengerId(this.getPassengerId());
    tripVO.setDriverId(this.getDriverId());
    tripVO.setStartPos(this.getStartPos().serialize());
    tripVO.setEndPos(this.getEndPos().serialize());
    tripVO.setState(this.getState().name());
    return tripVO;
  }
}

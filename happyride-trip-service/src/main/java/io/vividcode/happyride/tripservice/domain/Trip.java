package io.vividcode.happyride.tripservice.domain;

import io.eventuate.tram.events.aggregates.ResultWithDomainEvents;
import io.vividcode.happyride.common.BaseEntityWithGeneratedId;
import io.vividcode.happyride.common.Position;
import io.vividcode.happyride.tripservice.api.events.TripCreatedEvent;
import io.vividcode.happyride.tripservice.api.events.TripDetails;
import io.vividcode.happyride.tripservice.api.events.TripDomainEvent;
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

  public static ResultWithDomainEvents<Trip, TripDomainEvent> createTrip(String passengerId, Position startPos, Position endPos) {
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

  public Trip(String passengerId, Position startPos, Position endPos) {
    this.passengerId = passengerId;
    this.startPos = startPos;
    this.endPos = endPos;
    this.state = TripState.PENDING_DISPATCH;
  }
}

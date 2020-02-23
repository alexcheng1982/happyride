package io.vividcode.happyride.tripservice.domain;

import io.eventuate.tram.events.aggregates.ResultWithDomainEvents;
import io.vividcode.happyride.common.AbstractEntity;
import io.vividcode.happyride.common.Position;
import io.vividcode.happyride.tripservice.api.events.TripCreatedEvent;
import io.vividcode.happyride.tripservice.api.events.TripDetails;
import io.vividcode.happyride.tripservice.api.events.TripDomainEvent;
import java.util.UUID;
import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "trip")
@Data
@EqualsAndHashCode(of = "id", callSuper = false)
public class Trip extends AbstractEntity<String> {

  public static ResultWithDomainEvents<Trip, TripDomainEvent> createTrip(String passengerId, Position startPos, Position endPos) {
    Trip trip = new Trip(passengerId, startPos, endPos);
    TripCreatedEvent event = new TripCreatedEvent(new TripDetails(passengerId, startPos, endPos));
    return new ResultWithDomainEvents<>(trip, event);
  }

  @Id
  private String id;

  @Column(name = "passenger_id")
  private String passengerId;

  @Column(name = "driver_id")
  private String driverId;

  @Embedded
  @AttributeOverrides({
      @AttributeOverride(name = "lat", column = @Column(name = "start_pos_lat")),
      @AttributeOverride(name = "lng", column = @Column(name = "start_pos_lng")),
      @AttributeOverride(name = "addressName", column = @Column(name = "start_pos_address_name"))
  })
  private Position startPos;

  @Embedded
  @AttributeOverrides({
      @AttributeOverride(name = "lat", column = @Column(name = "end_pos_lat")),
      @AttributeOverride(name = "lng", column = @Column(name = "end_pos_lng")),
      @AttributeOverride(name = "addressName", column = @Column(name = "end_pos_address_name"))
  })
  private Position endPos;

  @Column(name = "state")
  @Enumerated(EnumType.STRING)
  private TripState state;

  @Column(name = "created_at")
  private Long createdAt;

  @Column(name = "updated_at")
  private Long updatedAt;

  public Trip(String passengerId, Position startPos, Position endPos) {
    this.passengerId = passengerId;
    this.startPos = startPos;
    this.endPos = endPos;
    this.state = TripState.PENDING_DISPATCH;
  }

  @PrePersist
  void prePersist() {
    id = UUID.randomUUID().toString();
    createdAt = System.currentTimeMillis();
    updatedAt = System.currentTimeMillis();
  }

  @PreUpdate
  void preUpdate() {
    updatedAt = System.currentTimeMillis();
  }
}

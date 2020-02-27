package io.vividcode.happyride.dispatcherservice.domain;

import io.eventuate.tram.events.aggregates.ResultWithDomainEvents;
import io.vividcode.happyride.common.EntityWithGeneratedId;
import io.vividcode.happyride.common.Position;
import io.vividcode.happyride.dispatcherservice.api.events.DispatchDomainEvent;
import io.vividcode.happyride.dispatcherservice.api.events.TripDispatchedEvent;
import io.vividcode.happyride.dispatcherservice.service.AvailableDriver;
import io.vividcode.happyride.tripservice.api.events.TripDetails;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "dispatches")
@NoArgsConstructor
@RequiredArgsConstructor
@Getter
@Setter
@ToString
public class Dispatch extends EntityWithGeneratedId {
  @NonNull
  @Column(name = "trip_id")
  private String tripId;

  @NonNull
  @Column(name = "start_pos_lng")
  private BigDecimal startPosLng;

  @NonNull
  @Column(name = "start_pos_lat")
  private BigDecimal startPosLat;

  @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
  @JoinColumn(name = "dispatch_id", referencedColumnName = "id", nullable = false)
  private Set<TripAcceptance> tripAcceptances = new HashSet<>();

  public static ResultWithDomainEvents<Dispatch, DispatchDomainEvent> createDispatch(String tripId, TripDetails tripDetails, Set<AvailableDriver> drivers) {
    Position startPos = tripDetails.getStartPos();
    Dispatch dispatch = new Dispatch(tripId, startPos.getLng(), startPos.getLat());
    Set<TripAcceptance> tripAcceptances = drivers.stream()
        .map(driver -> new TripAcceptance(driver.getDriverId(), driver.getLng(), driver.getLat()))
        .collect(Collectors.toSet());
    dispatch.setTripAcceptances(tripAcceptances);
    Set<String> driversId = drivers.stream().map(AvailableDriver::getDriverId).collect(Collectors.toSet());
    TripDispatchedEvent event = new TripDispatchedEvent(tripId, tripDetails, driversId);
    return new ResultWithDomainEvents<>(dispatch, event);
  }
}

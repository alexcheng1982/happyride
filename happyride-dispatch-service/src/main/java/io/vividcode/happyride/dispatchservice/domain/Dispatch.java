package io.vividcode.happyride.dispatchservice.domain;

import com.google.common.collect.Lists;
import io.eventuate.tram.events.aggregates.ResultWithDomainEvents;
import io.vividcode.happyride.common.EntityWithGeneratedId;
import io.vividcode.happyride.common.Position;
import io.vividcode.happyride.common.PositionView;
import io.vividcode.happyride.dispatchservice.AvailableDriver;
import io.vividcode.happyride.dispatchservice.api.events.DispatchDomainEvent;
import io.vividcode.happyride.dispatchservice.api.events.TripAcceptanceDeclinedEvent;
import io.vividcode.happyride.dispatchservice.api.events.TripAcceptanceDeclinedReason;
import io.vividcode.happyride.dispatchservice.api.events.TripAcceptanceSelectedEvent;
import io.vividcode.happyride.dispatchservice.api.events.TripDispatchFailedEvent;
import io.vividcode.happyride.dispatchservice.api.events.TripDispatchFailedReason;
import io.vividcode.happyride.dispatchservice.api.events.TripDispatchedEvent;
import io.vividcode.happyride.tripservice.api.events.DriverAcceptTripDetails;
import io.vividcode.happyride.tripservice.api.events.TripDetails;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Size;
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
  @Size(max = 36)
  private String tripId;

  @NonNull
  @Column(name = "start_pos_lng")
  private BigDecimal startPosLng;

  @NonNull
  @Column(name = "start_pos_lat")
  private BigDecimal startPosLat;

  @NonNull
  @Enumerated(EnumType.STRING)
  @Column(name = "state")
  private DispatchState state = DispatchState.WAIT_FOR_ACCEPTANCE;

  @Enumerated(EnumType.STRING)
  @Column(name = "failed_reason")
  private TripDispatchFailedReason failedReason;

  @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
  @JoinColumn(name = "dispatch_id", referencedColumnName = "id", nullable = false)
  private List<TripAcceptance> tripAcceptances = Lists.newArrayList();

  public static ResultWithDomainEvents<Dispatch, DispatchDomainEvent> createDispatch(String tripId,
      TripDetails tripDetails, Set<AvailableDriver> drivers) {
    PositionView startPos = tripDetails.getStartPos();
    Dispatch dispatch = new Dispatch(tripId, startPos.getLng(), startPos.getLat());
    List<TripAcceptance> tripAcceptances = drivers.stream()
        .map(driver -> new TripAcceptance(driver.getDriverId(), driver.getPosLng(),
            driver.getPosLat()))
        .collect(Collectors.toList());
    dispatch.setTripAcceptances(tripAcceptances);
    DispatchDomainEvent event;
    if (drivers.isEmpty()) {
      dispatch.setState(DispatchState.FAILED);
      dispatch.setFailedReason(TripDispatchFailedReason.NO_DRIVERS_AVAILABLE);
      event = new TripDispatchFailedEvent(tripId, TripDispatchFailedReason.NO_DRIVERS_AVAILABLE);
    } else {
      Set<String> driversId = drivers.stream().map(AvailableDriver::getDriverId)
          .collect(Collectors.toSet());
      event = new TripDispatchedEvent(tripId, tripDetails, driversId);
    }
    return new ResultWithDomainEvents<>(dispatch, event);
  }

  public Dispatch submitTripAcceptance(DriverAcceptTripDetails acceptTripDetails) {
    tripAcceptances.stream().filter(tripAcceptance -> Objects
        .equals(tripAcceptance.getDriverId(), acceptTripDetails.getDriverId()))
        .findFirst()
        .ifPresent(tripAcceptance -> {
          tripAcceptance.setState(TripAcceptanceState.SUBMITTED);
          tripAcceptance.setCurrentPosLng(acceptTripDetails.getPosLng());
          tripAcceptance.setCurrentPosLat(acceptTripDetails.getPosLat());
        });
    return this;
  }

  public ResultWithDomainEvents<Dispatch, DispatchDomainEvent> selectTripAcceptance(
      String driverId) {
    setState(DispatchState.ACCEPTANCE_SELECTED);
    Map<Boolean, List<TripAcceptance>> acceptances = tripAcceptances.stream()
        .collect(Collectors.groupingBy(tripAcceptance -> Objects
            .equals(tripAcceptance.getDriverId(), driverId)));
    List<TripAcceptance> toAccept = acceptances
        .getOrDefault(Boolean.TRUE, Collections.emptyList());
    toAccept.forEach(acceptance -> acceptance.setState(TripAcceptanceState.SELECTED));
    List<TripAcceptance> toDecline = acceptances
        .getOrDefault(Boolean.FALSE, Collections.emptyList());
    toDecline.forEach(acceptance -> acceptance.setState(TripAcceptanceState.DECLINED));
    List<DispatchDomainEvent> events = Lists.newArrayList();
    events.addAll(toAccept.stream()
        .map(acceptance -> new TripAcceptanceSelectedEvent(tripId, acceptance.getDriverId()))
        .collect(Collectors.toList()));
    events.addAll(toDecline.stream()
        .map(acceptance -> new TripAcceptanceDeclinedEvent(tripId, acceptance.getDriverId(),
            TripAcceptanceDeclinedReason.OTHER_SELECTED))
        .collect(Collectors.toList()));
    return new ResultWithDomainEvents<>(this, events.toArray(new DispatchDomainEvent[0]));
  }

  public ResultWithDomainEvents<Dispatch, DispatchDomainEvent> markAsFailed(String tripId,
      TripDispatchFailedReason reason) {
    setState(DispatchState.FAILED);
    setFailedReason(reason);
    TripDispatchFailedEvent event = new TripDispatchFailedEvent(tripId, reason);
    return new ResultWithDomainEvents<>(this, event);
  }
}

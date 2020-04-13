package io.vividcode.happyride.tripservice.cqrs.dataaccess;

import io.vividcode.happyride.common.PositionVO;
import io.vividcode.happyride.tripservice.api.TripState;
import io.vividcode.happyride.tripservice.api.events.TripDetails;
import io.vividcode.happyride.tripservice.cqrs.api.TripCancelledEvent;
import io.vividcode.happyride.tripservice.cqrs.api.TripConfirmedEvent;
import io.vividcode.happyride.tripservice.cqrs.api.TripCreatedEvent;
import javax.transaction.Transactional;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class TripViewEventHandler {

  @Autowired
  TripViewRepository repository;

  @EventHandler
  public void on(TripCreatedEvent event) {
    TripView tripView = new TripView();

    tripView.setId(event.getTripId());
    TripDetails tripDetails = event.getTripDetails();
    PositionVO startPos = tripDetails.getStartPos();
    tripView.setStartPosLng(startPos.getLng());
    tripView.setStartPosLat(startPos.getLat());
    PositionVO endPos = tripDetails.getEndPos();
    tripView.setEndPosLng(endPos.getLng());
    tripView.setEndPosLat(endPos.getLat());
    tripView.setState(TripState.CREATED);

    repository.save(tripView);
  }

  @EventHandler
  public void on(TripCancelledEvent event) {
    repository.findById(event.getTripId())
        .ifPresent(tripView -> tripView.setState(TripState.CANCELLED));
  }

  @EventHandler
  public void on(TripConfirmedEvent event) {
    repository.findById(event.getTripId())
        .ifPresent(tripView -> tripView.setState(TripState.CONFIRMED));
  }
}

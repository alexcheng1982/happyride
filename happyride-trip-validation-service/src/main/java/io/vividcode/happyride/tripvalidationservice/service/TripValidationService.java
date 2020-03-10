package io.vividcode.happyride.tripvalidationservice.service;

import io.vividcode.happyride.common.PositionView;
import io.vividcode.happyride.tripservice.api.events.TripDetails;
import io.vividcode.happyride.tripvalidationservice.AppConfig;
import org.locationtech.spatial4j.distance.DistanceUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TripValidationService {

  @Autowired
  private AppConfig appConfig;

  public void validateTrip(TripDetails tripDetails) {
    if (appConfig.isPassengerBlocked(tripDetails.getPassengerId())) {
      throw new PassengerBlockedException(tripDetails.getPassengerId());
    }

    double distance = calculateDistance(tripDetails);
    if (distance > appConfig.getTripDistanceLimit()) {
      throw new TripDistanceTooLongException(distance, appConfig.getTripDistanceLimit());
    }
  }

  private double calculateDistance(TripDetails tripDetails) {
    PositionView startPos = tripDetails.getStartPos();
    PositionView endPos = tripDetails.getEndPos();
    return DistanceUtils
        .distHaversineRAD(startPos.getLat().doubleValue(), startPos.getLng().doubleValue(),
            endPos.getLat().doubleValue(), endPos.getLng().doubleValue());
  }
}

package io.vividcode.happyride.tripvalidationservice.service;

import io.vividcode.happyride.common.Utils;
import io.vividcode.happyride.tripservice.api.events.TripDetails;
import io.vividcode.happyride.tripvalidationservice.AppConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TripValidationService {

  @Autowired
  AppConfig appConfig;

  public void validateTrip(final TripDetails tripDetails) {
    if (this.appConfig.isPassengerBlocked(tripDetails.getPassengerId())) {
      throw new PassengerBlockedException(tripDetails.getPassengerId());
    }

    final double distance = this.calculateDistance(tripDetails);
    if (distance > this.appConfig.getTripDistanceLimit()) {
      throw new TripDistanceTooLongException(distance,
          this.appConfig.getTripDistanceLimit());
    }
  }

  private double calculateDistance(final TripDetails tripDetails) {
    return Utils.calculateDistance(tripDetails.getStartPos(),
        tripDetails.getEndPos());
  }
}

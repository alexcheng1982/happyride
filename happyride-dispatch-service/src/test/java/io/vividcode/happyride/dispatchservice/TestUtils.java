package io.vividcode.happyride.dispatchservice;

import io.vividcode.happyride.common.Position;
import io.vividcode.happyride.tripservice.api.events.DriverAcceptTripDetails;
import io.vividcode.happyride.tripservice.api.events.TripDetails;
import java.math.BigDecimal;
import java.util.UUID;

public class TestUtils {

  public static String uuid() {
    return UUID.randomUUID().toString();
  }

  public static TripDetails tripDetails0() {
    return new TripDetails(uuid(), new Position(BigDecimal.ZERO, BigDecimal.ZERO),
        new Position(BigDecimal.ZERO, BigDecimal.ZERO));
  }

  public static AvailableDriver availableDriver0() {
    return new AvailableDriver(uuid(), BigDecimal.ZERO, BigDecimal.ZERO);
  }

  public static AvailableDriver availableDriver1() {
    return new AvailableDriver(uuid(), BigDecimal.valueOf(0.000001), BigDecimal.valueOf(0.000001));
  }

  public static DriverAcceptTripDetails acceptanceForDriver(AvailableDriver driver) {
    return new DriverAcceptTripDetails(driver.getDriverId(), driver.getPosLng(),
        driver.getPosLat());
  }
}

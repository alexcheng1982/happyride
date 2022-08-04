package io.vividcode.happyride.common;

import static org.locationtech.spatial4j.distance.DistanceUtils.EARTH_MEAN_RADIUS_KM;

import org.locationtech.spatial4j.distance.DistanceUtils;

public class Utils {

  private Utils() {
  }

  public static double calculateDistance(final PositionVO startPos,
      final PositionVO endPos) {
    final double radians = DistanceUtils
        .distHaversineRAD(startPos.getLat().doubleValue(),
            startPos.getLng().doubleValue(),
            endPos.getLat().doubleValue(), endPos.getLng().doubleValue());
    return DistanceUtils.radians2Dist(radians, EARTH_MEAN_RADIUS_KM);
  }
}

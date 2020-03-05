package io.vividcode.happyride.dispatchservice;

import io.vividcode.happyride.dispatchservice.api.events.DriverLocation;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.Circle;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.GeoResults;
import org.springframework.data.geo.Point;
import org.springframework.data.redis.connection.RedisGeoCommands.DistanceUnit;
import org.springframework.data.redis.connection.RedisGeoCommands.GeoLocation;
import org.springframework.data.redis.connection.RedisGeoCommands.GeoRadiusCommandArgs;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class DriverLocationService {
  @Autowired
  RedisTemplate<String, String> redisTemplate;

  private final String key = "available_drivers";
  public static final Distance searchRadius = new Distance(10, DistanceUnit.KILOMETERS);

  public void addAvailableDriver(DriverLocation location) {
    redisTemplate.opsForGeo()
        .add(key, new Point(location.getLng().doubleValue(), location.getLat().doubleValue()),
            location.getDriverId());
  }

  public void removeAvailableDriver(String driverId) {
    redisTemplate.opsForGeo().remove(key, driverId);
  }

  public Set<AvailableDriver> findAvailableDrivers(BigDecimal lng, BigDecimal lat) {
    GeoResults<GeoLocation<String>> results = redisTemplate.opsForGeo()
        .radius(key, new Circle(new Point(lng.doubleValue(), lat.doubleValue()), searchRadius),
            GeoRadiusCommandArgs.newGeoRadiusArgs().includeCoordinates());
    if (results != null) {
      return results.getContent().stream().filter(Objects::nonNull)
          .map(result -> {
            GeoLocation<String> content = result.getContent();
            Point point = content.getPoint();
            return new AvailableDriver(content.getName(),
                BigDecimal.valueOf(point.getX()), BigDecimal.valueOf(point.getY()));
          })
          .collect(Collectors.toSet());
    }
    return Collections.emptySet();
  }
}

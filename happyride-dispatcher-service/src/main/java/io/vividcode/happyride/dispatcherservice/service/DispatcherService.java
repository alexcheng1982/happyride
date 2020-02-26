package io.vividcode.happyride.dispatcherservice.service;

import io.eventuate.tram.messaging.producer.MessageProducer;
import io.vividcode.happyride.dispatcherservice.api.events.DriverLocation;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
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
public class DispatcherService {
  @Autowired
  RedisTemplate<String, String> redisTemplate;

  @Autowired
  MessageProducer messageProducer;

  private static final String key = "available_drivers";

  public void addAvailableDriver(DriverLocation location) {
      redisTemplate.opsForGeo()
          .add(key, new Point(location.getLng().doubleValue(), location.getLat().doubleValue()),
              location.getDriverId());
  }

  public void removeAvailableDriver(String driverId) {
    redisTemplate.opsForGeo().remove(key, driverId);
  }

  public List<AvailableDriver> findAvailableDrivers(double lng, double lat, double radius) {
    GeoResults<GeoLocation<String>> results = redisTemplate.opsForGeo()
        .radius(key, new Circle(new Point(lng, lat), new Distance(radius, DistanceUnit.METERS)),
            GeoRadiusCommandArgs.newGeoRadiusArgs().includeDistance());
    if (results != null) {
      return results.getContent().stream().filter(Objects::nonNull)
          .map(result -> new AvailableDriver(result.getContent().getName(), distanceInMeters(result.getDistance())))
          .collect(Collectors.toList());
    }
    return Collections.emptyList();
  }

  public void dispatchTrip() {

  }

  private double distanceInMeters(Distance distance) {
    return distance.in(DistanceUnit.METERS).getValue();
  }
}

package io.vividcode.happyride.tripvalidationservice;

import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties("app")
public class AppConfig {

  private List<String> blockedPassengers = new ArrayList<>();

  private double tripDistanceLimit = 100;

  public boolean isPassengerBlocked(final String passengerId) {
    return this.blockedPassengers.contains(passengerId);
  }
}

package io.vividcode.happyride.passengerwebapi;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "destination")
@Data
@NoArgsConstructor
public class DestinationConfig {

  private String passenger;

  private String address;
}

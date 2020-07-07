package io.vividcode.happyride.passengerwebapi.graphql;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "destination")
@Data
@NoArgsConstructor
public class ServiceDestinationConfig {

  private String passenger;

  private String address;
}

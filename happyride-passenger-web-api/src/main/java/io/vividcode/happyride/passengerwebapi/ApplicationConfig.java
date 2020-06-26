package io.vividcode.happyride.passengerwebapi;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(DestinationConfig.class)
public class ApplicationConfig {

}

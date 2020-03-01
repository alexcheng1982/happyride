package io.vividcode.happyride.tripvalidationservice;

import io.eventuate.tram.jdbckafka.TramJdbcKafkaConfiguration;
import io.vividcode.happyride.tripvalidationservice.domain.TripValidationServiceConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import({TripValidationServiceConfiguration.class, TramJdbcKafkaConfiguration.class})
public class TripValidationServiceApplication {

  public static void main(String[] args) {
    SpringApplication.run(TripValidationServiceApplication.class, args);
  }
}

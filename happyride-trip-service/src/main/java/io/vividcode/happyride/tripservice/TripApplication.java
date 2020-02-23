package io.vividcode.happyride.tripservice;

import io.eventuate.jdbckafka.TramJdbcKafkaConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import(TramJdbcKafkaConfiguration.class)
public class TripApplication {

  public static void main(String[] args) {
    SpringApplication.run(TripApplication.class, args);
  }
}

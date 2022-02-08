package io.vividcode.happyride.passengerservice;

import io.vividcode.happyride.passengerservice.dataaccess.PassengerRepository;
import io.vividcode.happyride.passengerservice.domain.PassengerService;
import io.vividcode.happyride.passengerservice.web.PassengerController;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackageClasses = {PassengerRepository.class,
    PassengerService.class, PassengerController.class})
public class PassengerTestApplication {

}

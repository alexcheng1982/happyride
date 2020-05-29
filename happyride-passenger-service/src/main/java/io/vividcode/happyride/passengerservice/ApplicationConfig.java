package io.vividcode.happyride.passengerservice;

import io.vividcode.happyride.passengerservice.messagehandlers.PassengerServiceMessageHandlersConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableJpaRepositories
@EnableTransactionManagement
@Import(PassengerServiceMessageHandlersConfiguration.class)
public class ApplicationConfig {

}

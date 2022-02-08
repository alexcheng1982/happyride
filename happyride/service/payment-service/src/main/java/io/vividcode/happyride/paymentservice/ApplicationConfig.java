package io.vividcode.happyride.paymentservice;

import io.eventuate.tram.spring.jdbckafka.TramJdbcKafkaConfiguration;
import io.vividcode.happyride.paymentservice.commandhandlers.PaymentCommandHandlersConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableJpaRepositories
@EnableTransactionManagement
@Import({
    TramJdbcKafkaConfiguration.class,
    PaymentCommandHandlersConfiguration.class})
public class ApplicationConfig {

}
